package pl.tapo24.ui.tariff

import android.provider.ContactsContract.Data
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.adapter.QuerySuggestionAdapter
import pl.tapo24.adapter.TariffDataAdapter
import pl.tapo24.data.EnginesType
import pl.tapo24.data.elastic.DataQueryFromSuggestion
import pl.tapo24.data.elastic.DataQueryToSuggestion
import pl.tapo24.db.TapoDb
import pl.tapo24.db.entity.LastSearch
import pl.tapo24.db.entity.Tariff
import pl.tapo24.infrastructure.NetworkClient
import pl.tapo24.infrastructure.NetworkClientElastic
import javax.inject.Inject

@HiltViewModel
class TariffViewModel @Inject constructor(
    private val tapoDb: TapoDb,
    private val networkClient: NetworkClient,
    private val networkClientElastic: NetworkClientElastic
) : ViewModel() {
    val tariffData: MutableLiveData<List<Tariff>> = MutableLiveData()
    val clickedSuggestion = MutableLiveData(false)
    val suggestionData: MutableLiveData<List<LastSearch>> = MutableLiveData()
    lateinit var adapter: TariffDataAdapter
    lateinit var adapterSuggestion: QuerySuggestionAdapter

    private val _text = MutableLiveData<String>().apply {
        value = "This is Tarrif"
    }
    val text: LiveData<String> = _text
    val querySuggestionList = MutableLiveData<DataQueryFromSuggestion?>()

    fun sendQuerySuggestion(suggestion: String) {
        viewModelScope.launch(Dispatchers.IO) {
            var responseBody: DataQueryFromSuggestion? =null
            async {
                val body = DataQueryToSuggestion(suggestion)
                val response = networkClientElastic.getSuggestionList(body)
                response.onSuccess {
                    responseBody = it
                }
            }.await()

            withContext(Dispatchers.Main) {
                if (responseBody != null) {
                    // prepare suggestion list
                    // querySuggestionList.value = responseBody
                    val listLastSearch: MutableList<LastSearch> = mutableListOf()
                    responseBody?.results?.documents?.forEach {
                        val item = LastSearch(it.suggestion,0,false)
                        listLastSearch.add(item)
                    }
                    suggestionData.value = listLastSearch

                }

            }
        }

    }
    fun clickOnSuggestion() {
        // in fragment close typing
        //sendQuery
        //add to history
        // add fielt to resonse ID list in sugesstion
        //TODO: extned db add aditional fielt times clicked and map it during often search and clicked on tariff
    }

    fun sendQuery() {

    }

    fun startApp() {
        // TODO: delete it for production
        viewModelScope.launch(Dispatchers.IO) {
            var list: List<Tariff> = listOf()
            async { list =tapoDb.tariffDb().getAll() }.await()
            if (list.isEmpty()) {
                val e1 = Tariff("doc1","Kategoria","H09", "art 118 KK",0,0,"Kto śmieci ten jest śmieciorz","Artykuł ustawy o ustawie ustaw jest w suatwaie","",10,"","100","100",false,"TET FIELD",true,EnginesType.New)
                val e2 = Tariff("doc2","Kategoria","H09", "art 118 KK",0,0,"Kto śmieci ten jest śmieciorz","Artykuł ustawy o ustawie ustaw jest w suatwaie","",10,"","100","100",false,"TET FIELD",true,EnginesType.New)
                val e3 = Tariff("doc3","Kategoria","H09", "art 118 KK",0,0,"Kto śmieci ten jest śmieciorz","Artykuł ustawy o ustawie ustaw jest w suatwaie","",10,"","100","100",false,"TET FIELD",true,EnginesType.New)
                val e4 = Tariff("doc4","Kategoria","H09", "art 118 KK",0,0,"Kto śmieci ten jest śmieciorz","Artykuł ustawy o ustawie ustaw jest w suatwaie","",10,"","100","100",false,"TET FIELD",true,EnginesType.New)
                val e5 = Tariff("doc5","Kategoria","H09", "art 118 KK",0,0,"Kto śmieci ten jest śmieciorz","Artykuł ustawy o ustawie ustaw jest w suatwaie","",10,"","100","100",false,"TET FIELD",true,EnginesType.New)
                async { tapoDb.tariffDb().insert(e1) }.await()
                async { tapoDb.tariffDb().insert(e2) }.await()
                async { tapoDb.tariffDb().insert(e3) }.await()
                async { tapoDb.tariffDb().insert(e4) }.await()
                async { tapoDb.tariffDb().insert(e5) }.await()

            }
            async { list =tapoDb.tariffDb().getAll() }.await()
            withContext(Dispatchers.Main) {
                if (!list.isEmpty()) {
                    tariffData.value = list
                }

            }
        }

    }
}