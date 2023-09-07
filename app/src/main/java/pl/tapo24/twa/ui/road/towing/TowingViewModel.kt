package pl.tapo24.twa.ui.road.towing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.adapter.TowingAdapter
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.dbData.entity.Towing
import javax.inject.Inject

@HiltViewModel
class TowingViewModel @Inject constructor(
    private val dataTapoDb: DataTapoDb
)  : ViewModel() {
    val data = MutableLiveData<List<Towing>>()
    lateinit var adapter: TowingAdapter



    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            var dataFromDb: List<Towing>? = null
            async{dataFromDb=dataTapoDb.towing().getAll()}.await()
            withContext(Dispatchers.Main) {
                if (dataFromDb != null)
                    data.value = dataFromDb!!
            }
        }
    }
}