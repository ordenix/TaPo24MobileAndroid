package pl.tapo24.twa.ui.settings.pdf

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.adapter.PdfListAdapter
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.dbData.entity.Law
import pl.tapo24.twa.updater.LawUpdater
import javax.inject.Inject

@HiltViewModel
class PdfListViewModel @Inject constructor(
    private val dataTapoDb: DataTapoDb,
    private val lawUpdater: LawUpdater
): ViewModel() {
    lateinit var adapter: PdfListAdapter

    val data: MutableLiveData<List<Law>> = MutableLiveData()

    init {
        getData()
    }

    fun changeSelectedStatus(law: Law) {
        viewModelScope.launch(Dispatchers.IO) {
            law.isSelectedToDownload = law.isSelectedToDownload != true
            if (law.isSelectedToDownload!!) {
                async {  lawUpdater.downloadOptionalFile(law) }.await()
            } else {
                async { lawUpdater.deleteOptionalFile(law) }.await()
            }

            getData()
        }
    }



    private fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            val dataFromDb = async { dataTapoDb.law().getAllByType("law") }.await()
            if (dataFromDb.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    data.value = dataFromDb
                }
            }
        }
    }
}