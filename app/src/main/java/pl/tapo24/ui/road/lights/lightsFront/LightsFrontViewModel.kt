package pl.tapo24.ui.road.lights.lightsFront

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.adapter.LightsFrontAdapter
import pl.tapo24.dbData.DataTapoDb
import pl.tapo24.dbData.entity.LightsFront
import javax.inject.Inject

@HiltViewModel
class LightsFrontViewModel @Inject constructor(
    private val dataTapoDb: DataTapoDb
) : ViewModel() {
    lateinit var adapter: LightsFrontAdapter
    val data = MutableLiveData<List<LightsFront>>()

    fun getData() {
        viewModelScope.launch(Dispatchers.IO){
            var dataFromDb: List<LightsFront>? = null
            async { dataFromDb = dataTapoDb.lightsFront().getAll() }.await()
            withContext(Dispatchers.Main) {
                data.value = dataFromDb!!
            }
        }
    }
}