package pl.tapo24.ui.road.uto.utoComponent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.dbData.DataTapoDb

import pl.tapo24.dbData.entity.Uto
import javax.inject.Inject

@HiltViewModel
class UtoComponentViewModel @Inject constructor(
    private val dataTapoDb: DataTapoDb
): ViewModel() {
    val data = MutableLiveData<Uto>()

    val expandRights = MutableLiveData(false)
    val expandWhereAllowed = MutableLiveData(false)
    val expandSpeed = MutableLiveData(false)
    val expandProhibition = MutableLiveData(false)
    val expandBehavior = MutableLiveData(false)
    val expandStop = MutableLiveData(false)


    fun getData(id: Int) {
        viewModelScope.launch(Dispatchers.IO){
            var dataFromDb: Uto? = null
            async { dataFromDb = dataTapoDb.uto().getById(id) }.await()
            withContext(Dispatchers.Main) {
                data.value = dataFromDb!!
            }
        }
    }

}