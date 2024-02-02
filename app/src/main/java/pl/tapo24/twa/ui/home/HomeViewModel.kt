package pl.tapo24.twa.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import pl.tapo24.twa.adapter.HomeWhatsNewsAdapter
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Setting
import pl.tapo24.twa.db.entity.WhatsNews
import pl.tapo24.twa.infrastructure.NetworkClient
import pl.tapo24.twa.useCase.homeNews.GetHomeNewsDataUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val tapoDb: TapoDb,
    private val networkClient: NetworkClient,
    private val getHomeNewsDataUseCase: GetHomeNewsDataUseCase
) : ViewModel() {

    lateinit var adapter: HomeWhatsNewsAdapter
    val whatsNews: MutableLiveData<List<WhatsNews>> = MutableLiveData()
    val navigateToInitPackage: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            var settingNetwork: Setting? = null
            async { settingNetwork = tapoDb.settingDb().getSettingByName("settingNetwork") }.await()
            if (settingNetwork == null) {
                withContext(Dispatchers.Main) {
                    navigateToInitPackage.value = true
                }
            }
        }

    }
    fun startApp() {
        viewModelScope.launch(Dispatchers.IO) {
            var list = async { getHomeNewsDataUseCase.getData() }.await()
            if (list != null) {
                withContext(Dispatchers.Main) {
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