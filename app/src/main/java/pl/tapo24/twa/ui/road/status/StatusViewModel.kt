package pl.tapo24.twa.ui.road.status

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.adapter.StatusAdapter
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.dbData.entity.Status
import javax.inject.Inject

@HiltViewModel
class StatusViewModel @Inject constructor(
    private val dataTapoDb: DataTapoDb
) : ViewModel() {
    lateinit var adapter: StatusAdapter
   val data = MutableLiveData<List<Status>>()

    fun getData() {
        viewModelScope.launch(Dispatchers.IO){
            var dataFromDb: List<Status>? = null
            async { dataFromDb = dataTapoDb.status().getAll() }.await()
            withContext(Dispatchers.Main) {
                data.value = dataFromDb!!
            }
        }
    }

    fun expand(position: Int) {
        data.value!![position].expand =  !data.value!![position].expand
        adapter.notifyDataSetChanged()
    }
}