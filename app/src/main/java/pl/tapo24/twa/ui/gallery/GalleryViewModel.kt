//package pl.tapo24.twa.ui.gallery
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import pl.tapo24.twa.db.TapoDb
//import pl.tapo24.twa.db.entity.Setting
//import pl.tapo24.twa.infrastructure.NetworkClient
//import javax.inject.Inject
//
//@HiltViewModel
//class GalleryViewModel @Inject constructor(
//    private val tapoDb: TapoDb,
//    private val networkClient: NetworkClient
//) : ViewModel() {
//
//    private val _text = MutableLiveData<String>().apply {
//        value = "This is gallery Fragment"
//    }
//    val text: LiveData<String> = _text
//
//    fun test() {
//        viewModelScope.launch(Dispatchers.IO) {
//            tapoDb.settingDb().insert(Setting("NAME","VALUE"))
//        }
//
//    }
//    fun test2() {
//        viewModelScope.launch(Dispatchers.IO) {
//            networkClient.getUid()
//        }
//
//    }
//}