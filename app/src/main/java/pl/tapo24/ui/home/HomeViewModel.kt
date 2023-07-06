package pl.tapo24.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.adapter.HomeWhatsNewsAdapter
import pl.tapo24.data.Uid
import pl.tapo24.db.TapoDb
import pl.tapo24.db.entity.Setting
import pl.tapo24.db.entity.WhatsNews
import pl.tapo24.infrastructure.NetworkClient
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val tapoDb: TapoDb,
    private val networkClient: NetworkClient
) : ViewModel() {

    lateinit var adapter: HomeWhatsNewsAdapter
    val whatsNews: MutableLiveData<List<WhatsNews>> = MutableLiveData()

    fun startApp() {
        viewModelScope.launch(Dispatchers.IO) {
            var list: List<WhatsNews> = listOf()
            // TODO: DELETE IT IN PRODUCITON
            async { list = tapoDb.whatsNewsDb().getAll() }.await()
            if (list.isEmpty()) {
                val e1 = WhatsNews(1,1688548351, "Co nowego?", "-dodano pierdoły", "", "","","Stopka nanan", "stopka zwykłą", "Ktoś napewno")
                val e2 = WhatsNews(2,1688548351, "Co nowego?", "-dodano pierdoły", "- i jeszcze poprawono wczesniejsze pierdoły", "","","Stopka nanan", "stopka zwykłą", "Ktoś napewno")
                val e3 = WhatsNews(3,1688548351, "Co nowego?", "-dodano pierdoły1", "- i jeszcze poprawono wczesniejsze pierdoły", "","","Stopka nanan", "stopka zwykłą", "Ktoś napewno")
                val e4 = WhatsNews(4,1688548351, "Co nowego?", "-dodano pierdoły2", "- i jeszcze poprawono wczesniejsze pierdoły", "","","Stopka nanan", "stopka zwykłą", "Ktoś napewno")
                val e5 = WhatsNews(5,1688548351, "Co nowego?", "-dodano pierdoły3", "- i jeszcze poprawono wczesniejsze pierdoły", "","","Stopka nanan", "stopka zwykłą", "Ktoś napewno")
                val e6 = WhatsNews(6,1688548351, "Co nowego?", "-dodano pierdoły4", "- i jeszcze poprawono wczesniejsze pierdoły", "","","Stopka nanan", "stopka zwykłą", "Ktoś napewno")
                val e7 = WhatsNews(7,1688548351, "Co nowego?", "-dodano pierdoły5", "- i jeszcze poprawono wczesniejsze pierdoły", "","","Stopka nanan", "stopka zwykłą", "Ktoś napewno")

                async { tapoDb.whatsNewsDb().insert(e1) }.await()
                async { tapoDb.whatsNewsDb().insert(e2) }.await()
                async { tapoDb.whatsNewsDb().insert(e3) }.await()
                async { tapoDb.whatsNewsDb().insert(e4) }.await()
                async { tapoDb.whatsNewsDb().insert(e5) }.await()
                async { tapoDb.whatsNewsDb().insert(e6) }.await()
                async { tapoDb.whatsNewsDb().insert(e7) }.await()
            }
            async { list = tapoDb.whatsNewsDb().getAll() }.await()
            withContext(Dispatchers.Main) {
                if (!list.isEmpty()) {
                    whatsNews.value = list
                }
            }
        }
    }

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is home Fragment"
//    }
//    val text: LiveData<String> = _text
}