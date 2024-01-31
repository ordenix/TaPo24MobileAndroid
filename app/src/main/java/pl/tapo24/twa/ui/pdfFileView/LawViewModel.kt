package pl.tapo24.twa.ui.pdfFileView

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.adapter.PdfAdapter
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.dbData.entity.Law
import javax.inject.Inject
@HiltViewModel

class LawViewModel @Inject constructor(
    private val dataTapoDb: DataTapoDb,
    private val tapoDb: TapoDb
): ViewModel() {
    val data = MutableLiveData<List<Law>>()

    var searchText: String = ""
    lateinit var adapter: PdfAdapter
    var isPublicStorage = false


    init {
        viewModelScope.launch(Dispatchers.IO) {
            isPublicStorage = tapoDb.settingDb().getSettingByName("publicStorage")?.state ?: false

        }
    }

    fun getData(type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            var dataFromDb: List<Law>? = null
            async{dataFromDb = dataTapoDb.law().getAllByType(type)}.await()
            withContext(Dispatchers.Main) {
                if (dataFromDb != null) {
                    data.value = dataFromDb!!
                }
            }
        }
    }

}