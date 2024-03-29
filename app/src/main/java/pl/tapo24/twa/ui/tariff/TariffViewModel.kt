package pl.tapo24.twa.ui.tariff

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import pl.tapo24.twa.module.FavouriteModule
import pl.tapo24.twa.adapter.CustomCategoryMapAdapter
import pl.tapo24.twa.adapter.QuerySuggestionAdapter
import pl.tapo24.twa.adapter.TariffDataAdapter
import pl.tapo24.twa.data.EnginesType
import pl.tapo24.twa.data.EnvironmentType
import pl.tapo24.twa.data.NetworkTypes
import pl.tapo24.twa.data.State
import pl.tapo24.twa.data.customCategory.CategoryDictionary
import pl.tapo24.twa.data.customCategory.CategoryToMap
import pl.tapo24.twa.data.customCategory.DataCategory
import pl.tapo24.twa.data.elastic.DataClickToElastic
import pl.tapo24.twa.data.elastic.DataQueryFromSuggestion
import pl.tapo24.twa.data.elastic.DataQueryToSuggestion
import pl.tapo24.twa.data.elastic.queryToElasticForTariffList.DataToElasticForTariffList
import pl.tapo24.twa.data.elastic.queryToElasticForTariffList.Page
import pl.tapo24.twa.data.elastic.resultClasFromElastic.DataTariffListFromElastic
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.LastSearch
import pl.tapo24.twa.db.entity.MapCategory
import pl.tapo24.twa.db.entity.Setting
import pl.tapo24.twa.db.entity.Tariff
import pl.tapo24.twa.infrastructure.NetworkClient
import pl.tapo24.twa.infrastructure.NetworkClientElastic
import pl.tapo24.twa.module.CustomCategoryModule
import pl.tapo24.twa.useCase.customCategoryMap.PrepareMapListToTariffUseCase
import pl.tapo24.twa.useCase.customCategoryMap.SetMapCustomCategoryUseCase
import pl.tapo24.twa.utils.RegexTariff
import javax.inject.Inject

@HiltViewModel
class TariffViewModel @Inject constructor(
    private val tapoDb: TapoDb,
    private val networkClient: NetworkClient,
    private val networkClientElastic: NetworkClientElastic,
    private val favouriteModule: FavouriteModule,
    private val customCategoryModule: CustomCategoryModule,
    private val prepareMapListToTariffUseCase: PrepareMapListToTariffUseCase,
    private val setMapCustomCategoryUseCase: SetMapCustomCategoryUseCase,
) : ViewModel() {
    //val sss = tapoDb.tariffDb().getAll()
    var tariffData: MutableLiveData<List<Tariff>> = MutableLiveData()
    val tariffDataAll: MutableLiveData<List<Tariff>> = MutableLiveData()
    val clickedSuggestion = MutableLiveData(false)
    val suggestionData: MutableLiveData<List<LastSearch>> = MutableLiveData()
    lateinit var adapter: TariffDataAdapter
    lateinit var adapterSuggestion: QuerySuggestionAdapter
    val showDialog = MutableLiveData(false)
    var showDialogRenew = false
    var itemToDialog: Tariff? = null
    var positionToDialog = 0
    val clickedOnSuggestion: MutableLiveData<String> = MutableLiveData()
    val queryTextInSearchBar = MutableLiveData("")
    val checkFavourite: MutableLiveData<Boolean> = MutableLiveData(false)

    lateinit var adapterCustomCategoryMap: CustomCategoryMapAdapter
    val showEditMapDialog: MutableLiveData<Boolean> = MutableLiveData(false)
    val mapListCustomCategory: MutableLiveData<List<CategoryToMap>> = MutableLiveData()

    private val _text = MutableLiveData<String>().apply {
        value = "This is Tarrif"
    }
    val text: LiveData<String> = _text
    val querySuggestionList = MutableLiveData<DataQueryFromSuggestion?>()
    var categoryValue: DataCategory = CategoryDictionary.All.element

    init {
        if (State.premiumVersion) {
            viewModelScope.launch(Dispatchers.IO) {
                getListCustomCategoryMap()
                val listCustomCategory = async {customCategoryModule.getCustomCategories()}.await()
                listCustomCategory.onSuccess {elements ->
                    withContext(Dispatchers.Main) {
                        State.customCategoryList = elements
                    }
                }
                val showOnTop = async { customCategoryModule.getShowOnTopParameter() }.await()
                withContext(Dispatchers.Main) {
                    State.showCustomOnTop.value = showOnTop
                }

            }

        }
    }

    private suspend fun getListCustomCategoryMap() {
        viewModelScope.async(Dispatchers.IO) {
            val listCustomCategoryMap = async { customCategoryModule.getCustomCategoryMapList() }.await()

            listCustomCategoryMap.onSuccess {elements ->
                withContext(Dispatchers.Main) {
                    State.customCategoryMapList = elements
                }
            }

        }.await()
    }
    fun clickOnNotifyForTariffIcon() {
        State.showNotifyForTariffIcon = false
        viewModelScope.launch(Dispatchers.IO) {
            val setting: Setting = Setting("showNotifyForTariffIcon","",0,false)
            tapoDb.settingDb().insert(setting)
        }
        adapter.notifyDataSetChanged()

    }
    fun getListForDialogMap(tariffId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = async { prepareMapListToTariffUseCase.run(tariffId) }.await()
            withContext(Dispatchers.Main) {
                mapListCustomCategory.value = list
            }
        }
    }

    fun setMapCustomCategory(categoryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            async { setMapCustomCategoryUseCase.runMap(itemToDialog?.id ?: "", categoryId) }.await()
            async { getListCustomCategoryMap() }.await()
            if (categoryValue.isCustom) {
                withContext(Dispatchers.Main) {
                    tariffData.value = emptyList()
                }
                getAllTariffData()
            }
        }

    }

    fun saveShiftedItems() {
        viewModelScope.launch(Dispatchers.IO) {
            tariffDataAll.value!!.forEachIndexed { index, tariff ->
                tariff.sortOrderFav = index

            }
            async { tapoDb.tariffDb().insertList(tariffDataAll.value!!) }.await()
            withContext(Dispatchers.Main) {
                tariffData.value = tariffDataAll.value
            }
            favouriteModule.performSendFavList()
        }
    }

    fun sendQuerySuggestion(suggestion: String) {
        viewModelScope.launch(Dispatchers.IO) {
            var responseBody: DataQueryFromSuggestion? =null
            val listLastSearch: MutableList<LastSearch> = mutableListOf()
            if (State.internetStatus.value != NetworkTypes.None) {
                async {
                    val body = DataQueryToSuggestion(suggestion)
                    val response = networkClientElastic.getSuggestionList(body)
                    response.onSuccess {
                        responseBody = it
                    }
                }.await()
            }
            var historySuggestion: MutableList<LastSearch> = mutableListOf()
            if (suggestion.isEmpty()) {
                async { historySuggestion = tapoDb.lastSearchDb().getAllTop6ByTimes().toMutableList() }.await()
            } else {
                async { historySuggestion = tapoDb.lastSearchDb().getAllTop6ByTimesAndQuery(suggestion).toMutableList() }.await()
            }
            historySuggestion.forEach {
                listLastSearch.add(it)
            }
            withContext(Dispatchers.Main) {
                if (responseBody != null) {
                    // prepare suggestion list
                    // querySuggestionList.value = responseBody

                    responseBody?.results?.documents?.forEach {
                        val item = LastSearch(it.suggestion,0,false,0,"")
                        listLastSearch.add(item)
                    }

                }
                suggestionData.value = listLastSearch

            }
        }

    }
    fun clickOnSuggestion(clickedItem: LastSearch) {
        clickedOnSuggestion.value = clickedItem.query
    }


    private suspend fun sendQueryToElasticAndSave(queryText: String):List<String> {
        val listDocId = mutableListOf<String>()
        viewModelScope.async(Dispatchers.IO) {
            var responseFromElastic: DataTariffListFromElastic? = null
            val page = Page()
            val dataToElastic = DataToElasticForTariffList(query = queryText, page = page)
            async {
                val result = networkClientElastic.getTariffDataList(dataToElastic)
                result.onSuccess {
                    responseFromElastic = it
                }
            }.await()
            responseFromElastic?.results?.forEach {
                listDocId.add(it.id.raw)
            }
            val listDocString: String = listDocId.joinToString("/")
            val lastSearchFromDb = tapoDb.lastSearchDb().getByQuery(queryText)
            if  (lastSearchFromDb != null) {
                lastSearchFromDb.count += 1
                lastSearchFromDb.listDocId = listDocString
                lastSearchFromDb.lastTime = System.currentTimeMillis()
                tapoDb.lastSearchDb().insert(lastSearchFromDb)
            } else {
                if (listDocString.length > 1) {
                    val lastSearch = LastSearch(queryText,System.currentTimeMillis(),true,1,listDocString)
                    tapoDb.lastSearchDb().insert(lastSearch)
                }

            }
        }.await()
        return  listDocId
    }
    fun clickOnDeleteHistorySuggestion(clickedItem: LastSearch) {
        viewModelScope.launch(Dispatchers.IO) {
            async {tapoDb.lastSearchDb().deleteElement(clickedItem)}.await()
            sendQuerySuggestion("")
        }

    }

    fun searchTariffData(queryText: String) {
        // TODO MAT24-23 Rozbudowa wyszukiwarki w taryfikatorze
        viewModelScope.launch(Dispatchers.IO) {
            if (queryText.isNotEmpty()) {
                if (RegexTariff().code(queryText)) {
                    // its code
                    withContext(Dispatchers.Main) {
                        val codeToFilter = queryText.replaceRange(1,1," ")
                        tariffData.value = tariffDataAll.value?.filter { element -> element.code?.contains(codeToFilter, true)
                            ?: false }
                    }
                } else if (RegexTariff().sign(queryText)) {
                    //its sign
                    withContext(Dispatchers.Main) {
                        var q = ""
                        q = if (queryText.length>3) {
                            queryText.replaceRange(4,4," ")
                        } else {
                            queryText.replaceRange(3,3," ")
                        }

                        tariffData.value = tariffDataAll.value?.filter { element -> element.name?.contains(q, true)
                            ?: false }
                    }

                } else {
                    // other result
                    var docIdArray = listOf<String>()
                    var listTariffToPost = mutableListOf<Tariff>()
                    if (State.internetStatus.value != NetworkTypes.None) {
                        // filter online

                        async { docIdArray = sendQueryToElasticAndSave(queryText) }.await()



                    } else {
                        // FILTER OFFLINE
                        val lastSearchFromDb = tapoDb.lastSearchDb().getByQuery(queryText)
                        if (lastSearchFromDb !=null) {
                            docIdArray = lastSearchFromDb.listDocId.split("/")
                        } else {
                            listTariffToPost = tariffDataAll.value?.filter { element -> element.name?.contains(queryText,true)
                                ?: false } as MutableList<Tariff>
                        }

                    }
                    if (docIdArray.isNotEmpty()) {

                        docIdArray.forEach {
                            tariffDataAll.value?.find { element -> element.id == it }?.let { it1 ->
                                listTariffToPost.add(
                                    it1
                                )
                            }
                        }

                    }


                    withContext(Dispatchers.Main) {
                        tariffData.value = listTariffToPost
                    }



                }


            } else {
                // clear filter
                withContext(Dispatchers.Main ) {
                    if (tariffDataAll.value?.isNotEmpty() == true) {
                        tariffData.value = tariffDataAll.value

                    } else {
                        // MAT24-34 java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
                        tariffData.value = emptyList()
                    }
                }
            }

        }
    }
    fun clickMore(item: Tariff, position: Int) {
        itemToDialog = item
        positionToDialog = position
        showDialog.value = true
        if (State.internetStatus.value != NetworkTypes.None && queryTextInSearchBar.value?.isNotEmpty() == true) {
            val dataToClick = DataClickToElastic(item.id,queryTextInSearchBar.value.orEmpty())
            viewModelScope.launch(Dispatchers.IO) {
                val r = networkClientElastic.clickData(dataToClick)
                println(r)
            }

        }


    }
    fun clickOnFavorites(item: Tariff, position: Int) {
        item.favorites = !item.favorites
        viewModelScope.launch(Dispatchers.IO) {
            async {tapoDb.tariffDb().insert(item) }
        }
        favouriteModule.performSendFavList()
        adapter.notifyItemChanged(position)
    }
    fun changeFavState() {
        checkFavourite.value = !checkFavourite.value!!
        viewModelScope.launch(Dispatchers.IO) {
            val setting: Setting = Setting("checkedFav","",0,checkFavourite.value!!)
            //tapoDb.settingDb().insert(setting) TODO
            async { getAllTariffData() }.await()
            searchTariffData(queryTextInSearchBar.value.orEmpty())
        }
    }

    fun getDataOnCategoryChanged() {
        viewModelScope.launch(Dispatchers.IO) {
            async { getAllTariffData() }.await()
            searchTariffData(queryTextInSearchBar.value.orEmpty())
        }
    }

    private suspend fun getAllTariffData() {
        var dataFromDb: List<Tariff>? = null
        viewModelScope.async(Dispatchers.IO) {
            if (State.enginesType == EnginesType.New) {
                if (checkFavourite.value == true) {
                    async { dataFromDb = tapoDb.tariffDb().getFavByEngine("New") }.await()
                } else {
                    if (categoryValue.isCustom) {
                        val tarifIds:List<MapCategory>  = State.customCategoryMapList.filter {
                            mapCategory -> mapCategory.customCategoryId == categoryValue.customId
                        }
                        val listToAdd: MutableList<Tariff> = mutableListOf()
                        async {
                            tarifIds.forEach { element ->
                                listToAdd.add(tapoDb.tariffDb().getByTariffId(element.tariffId))
                            }
                        }.await()
                        dataFromDb = listToAdd
                    } else {
                        async { dataFromDb = tapoDb.tariffDb().getAllByEngineAndCategory("New", categoryValue.query) }.await()

                    }
                }

            } else {
                if (checkFavourite.value == true) {
                    async { dataFromDb = tapoDb.tariffDb().getFavByEngine("Old") }.await()
                } else {
                    async { dataFromDb = tapoDb.tariffDb().getAllByEngineAndCategory("Old", categoryValue.query) }.await()
                }
            }

            withContext(Dispatchers.Main) {
                if (dataFromDb != null) {
                    if ((tariffData.value?.size ?: 0) < 1) {
                        tariffData.value = dataFromDb!!
                    }
                    tariffDataAll.value = dataFromDb!!

                }
            }
        }.await()
    }

    fun startApp() {
        viewModelScope.launch(Dispatchers.IO) {
            var checked: Boolean? = null
            async { checked= tapoDb.settingDb().getSettingByName("checkedFav")?.state }.await()
            withContext(Dispatchers.Main) {
                if (checked == null) {
                    checkFavourite.value = false
                } else {
                    checkFavourite.value = checked!!
                }
            }
            getAllTariffData()


        }
            var list: List<Tariff> = listOf()
//            async { list =tapoDb.tariffDb().getAll() }.await()
//            withContext(Dispatchers.Main) {
//                if (!list.isEmpty()) {
//                    _tariffData.value = list
//                }
//
//            }


    }
}