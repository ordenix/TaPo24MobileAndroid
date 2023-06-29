package pl.tapo24.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pl.tapo24.data.Uid
import pl.tapo24.db.TapoDb
import pl.tapo24.db.entity.Setting
import pl.tapo24.infrastructure.NetworkClient
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val tapoDb: TapoDb,
    private val networkClient: NetworkClient
) : ViewModel() {

    fun startApp() {
//        viewModelScope.launch(Dispatchers.IO) {
//            var settingUid: Setting? = null
//            async { settingUid = tapoDb.settingDb().getSettingByName("uid") }.await()
//
//            if (settingUid == null) {
//                async {
//                    val response = networkClient.getUid()
//                    println(response)
//                    response.onSuccess {
//                        val setting = Setting("uid",it.uid!!)
//                        tapoDb.settingDb().insert(setting)
//                    }
//
//                }
//            }
//        }

    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}