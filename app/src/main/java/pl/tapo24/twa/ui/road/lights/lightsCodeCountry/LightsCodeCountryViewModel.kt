package pl.tapo24.twa.ui.road.lights.lightsCodeCountry

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.adapter.LightsCodeCountryAdapter
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.dbData.entity.LightsCodeCountry
import javax.inject.Inject

@HiltViewModel
class LightsCodeCountryViewModel @Inject constructor(
    private val dataTapoDb: DataTapoDb
) : ViewModel() {
    lateinit var adapter: LightsCodeCountryAdapter
    val data = MutableLiveData<List<LightsCodeCountry>>()



    fun getData() {
        viewModelScope.launch(Dispatchers.IO){
            var dataFromDb: List<LightsCodeCountry>? = null
            async { dataFromDb = dataTapoDb.lightsCodeCountry().getAll() }.await()
            withContext(Dispatchers.Main) {
                data.value = dataFromDb!!
            }
        }
    }
}