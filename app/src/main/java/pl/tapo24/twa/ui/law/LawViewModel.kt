package pl.tapo24.twa.ui.law

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.adapter.PdfAdapter
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.dbData.entity.Law
import javax.inject.Inject
@HiltViewModel

class LawViewModel @Inject constructor(
    private val dataTapoDb: DataTapoDb
): ViewModel() {
    val data = MutableLiveData<List<Law>>()

    var searchText: String = ""
    lateinit var adapter: PdfAdapter

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            var dataFromDb: List<Law>? = null
            async{dataFromDb = dataTapoDb.law().getAllByType("law")}.await()
            withContext(Dispatchers.Main) {
                if (dataFromDb != null) {
                    data.value = dataFromDb!!
                }
            }
        }
    }

}