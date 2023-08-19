package pl.tapo24.ui.road.lights.lightsOthres

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.adapter.LightsOthersAdapter
import pl.tapo24.dbData.DataTapoDb
import pl.tapo24.dbData.entity.LightsOthers
import javax.inject.Inject

@HiltViewModel
class LightsOthersViewModel @Inject constructor(
    private val dataTapoDb: DataTapoDb
) : ViewModel() {

    lateinit var adapter: LightsOthersAdapter
    val data = MutableLiveData<List<LightsOthers>>()

    fun getData() {
        viewModelScope.launch(Dispatchers.IO){
            var dataFromDb: List<LightsOthers>? = null
            async { dataFromDb = dataTapoDb.lightsOthers().getAll() }.await()
            withContext(Dispatchers.Main) {
                data.value = dataFromDb!!
            }
        }
    }
}