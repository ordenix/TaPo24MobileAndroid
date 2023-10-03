package pl.tapo24.twa.ui.tariff

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import pl.tapo24.twa.adapter.QuerySuggestionAdapter
import pl.tapo24.twa.adapter.TariffDataAdapter
import pl.tapo24.twa.data.EnginesType
import pl.tapo24.twa.data.State
import pl.tapo24.twa.data.elastic.DataQueryFromSuggestion
import pl.tapo24.twa.data.elastic.DataQueryToSuggestion
import pl.tapo24.twa.data.elastic.queryToElasticForTariffList.DataToElasticForTariffList
import pl.tapo24.twa.data.elastic.queryToElasticForTariffList.Page
import pl.tapo24.twa.data.elastic.resultClasFromElastic.DataTariffListFromElastic
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.LastSearch
import pl.tapo24.twa.db.entity.Setting
import pl.tapo24.twa.db.entity.Tariff
import pl.tapo24.twa.infrastructure.NetworkClient
import pl.tapo24.twa.infrastructure.NetworkClientElastic
import pl.tapo24.twa.utils.RegexTariff
import javax.inject.Inject

@HiltViewModel
class TariffViewModel @Inject constructor(
    private val tapoDb: TapoDb,
    private val networkClient: NetworkClient,
    private val networkClientElastic: NetworkClientElastic
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

    private val _text = MutableLiveData<String>().apply {
        value = "This is Tarrif"
    }
    val text: LiveData<String> = _text
    val querySuggestionList = MutableLiveData<DataQueryFromSuggestion?>()


    fun saveShiftedItems() {
        viewModelScope.launch(Dispatchers.IO) {
            tariffDataAll.value!!.forEachIndexed { index, tariff ->
                tariff.sortOrderFav = index

            }
            async { tapoDb.tariffDb().insertList(tariffDataAll.value!!) }.await()
            withContext(Dispatchers.Main) {
                tariffData.value = tariffDataAll.value
            }
        }
    }

    fun sendQuerySuggestion(suggestion: String) {
        viewModelScope.launch(Dispatchers.IO) {
            var responseBody: DataQueryFromSuggestion? =null
            val listLastSearch: MutableList<LastSearch> = mutableListOf()
            if (State.internetStatus.value != 0) {
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
        // TODO
        // add offline search
        // first catch a rexex
        // add filter category
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
                    if (State.internetStatus.value != 0) {
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
                        tariffData.value = listTariffToPost.take(State.maxVisibleItem)
                    }



                }


            } else {
                // clear filter
                withContext(Dispatchers.Main ) {
                    if (tariffDataAll.value?.isNotEmpty() == true) {
                        tariffData.value = tariffDataAll.value?.take(State.maxVisibleItem)

                    }
                }
            }

        }
    }
    fun clickMore(item: Tariff, position: Int) {
        itemToDialog = item
        positionToDialog = position
        showDialog.value = true

    }
    fun clickOnFavorites(item: Tariff, position: Int) {
        item.favorites = !item.favorites
        viewModelScope.launch(Dispatchers.IO) {
            async {tapoDb.tariffDb().insert(item) }
        }
        adapter.notifyItemChanged(position)
    }
    fun changeFavState() {
        checkFavourite.value = !checkFavourite.value!!
        viewModelScope.launch(Dispatchers.IO) {
            val setting: Setting = Setting("checkedFav","",0,checkFavourite.value!!)
            tapoDb.settingDb().insert(setting)
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
                    async { dataFromDb = tapoDb.tariffDb().getAllByEngine("New") }.await()
                }

            } else {
                if (checkFavourite.value == true) {
                    async { dataFromDb = tapoDb.tariffDb().getFavByEngine("Old") }.await()
                } else {
                    async { dataFromDb = tapoDb.tariffDb().getAllByEngine("Old") }.await()
                }
            }

            withContext(Dispatchers.Main) {
                if (dataFromDb != null) {
                    if ((tariffData.value?.size ?: 0) < 1) {
                        tariffData.value = dataFromDb!!.take(State.maxVisibleItem)
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