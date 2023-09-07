package pl.tapo24.twa.ui.tariff

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.adapter.QuerySuggestionAdapter
import pl.tapo24.twa.adapter.TariffDataAdapter
import pl.tapo24.twa.data.State
import pl.tapo24.twa.data.elastic.DataQueryFromSuggestion
import pl.tapo24.twa.data.elastic.DataQueryToSuggestion
import pl.tapo24.twa.data.elastic.queryToElasticForTariffList.DataToElasticForTariffList
import pl.tapo24.twa.data.elastic.queryToElasticForTariffList.Page
import pl.tapo24.twa.data.elastic.resultClasFromElastic.DataTariffListFromElastic
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.LastSearch
import pl.tapo24.twa.db.entity.Tariff
import pl.tapo24.twa.infrastructure.NetworkClient
import pl.tapo24.twa.infrastructure.NetworkClientElastic
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

    private val _text = MutableLiveData<String>().apply {
        value = "This is Tarrif"
    }
    val text: LiveData<String> = _text
    val querySuggestionList = MutableLiveData<DataQueryFromSuggestion?>()

    fun sendQuerySuggestion(suggestion: String) {
        viewModelScope.launch(Dispatchers.IO) {
            var responseBody: DataQueryFromSuggestion? =null
            val listLastSearch: MutableList<LastSearch> = mutableListOf()
            if (State.internetStatus != 0) {
                async {
                    val body = DataQueryToSuggestion(suggestion)
                    val response = networkClientElastic.getSuggestionList(body)
                    response.onSuccess {
                        responseBody = it
                    }
                }.await()
            }
            var historySuggestion: MutableList<LastSearch> = mutableListOf()
            async { historySuggestion = tapoDb.lastSearchDb().getAll().toMutableList() }.await()
            historySuggestion.forEach {
                listLastSearch.add(it)
            }
            withContext(Dispatchers.Main) {
                if (responseBody != null) {
                    // prepare suggestion list
                    // querySuggestionList.value = responseBody

                    responseBody?.results?.documents?.forEach {
                        val item = LastSearch(it.suggestion,0,false)
                        listLastSearch.add(item)
                    }

                }
                suggestionData.value = listLastSearch

            }
        }

    }
    fun clickOnSuggestion(clickedItem: LastSearch) {
        clickedOnSuggestion.value = clickedItem.query
        //add to history
        // add fielt to resonse ID list in sugesstion
        //TODO: extned db add aditional fielt times clicked and map it during often search and clicked on tariff
    }

    fun sendQueryAndSearchTariffData(queryText: String) {
        // TODO
        // first catch a rexex https://regex-generator.olafneumann.org/?sampleText=b02&flags=i&selection=1%7CNumber,0%7CMultiple%20characters
        //https://regexr.com/
        // add filter favor and category
        viewModelScope.launch(Dispatchers.IO) {
            if (queryText.isNotEmpty()) {
                var responseFromElastic: DataTariffListFromElastic? = null
                if (State.internetStatus != 0) {
                    val page = Page()
                    val dataToElastic = DataToElasticForTariffList(query = queryText, page = page)
                    async {
                        val result = networkClientElastic.getTariffDataList(dataToElastic)
                        result.onSuccess {
                            responseFromElastic = it
                        }
                    }.await()
                    val listTariffToPost = mutableListOf<Tariff>()
                    responseFromElastic?.results?.forEach {
                        tariffDataAll.value?.find { element -> element.id == it.id.raw }?.let { it1 ->
                            listTariffToPost.add(
                                it1

                            )
                        }
                    }
                    withContext(Dispatchers.Main) {
                        tariffData.value = listTariffToPost.take(State.maxVisibleItem)
                    }
                } else {
                    // FILTER OFFLINE
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

    fun startApp() {
        // TODO: delete it for production
        viewModelScope.launch(Dispatchers.IO) {
            var dataFromDb: List<Tariff>? = null
            async { dataFromDb = tapoDb.tariffDb().getAll() }.await()
            withContext(Dispatchers.Main) {
                if (dataFromDb != null) {
                    tariffDataAll.value = dataFromDb!!
                    tariffData.value = dataFromDb!!.take(State.maxVisibleItem)
                }
            }


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