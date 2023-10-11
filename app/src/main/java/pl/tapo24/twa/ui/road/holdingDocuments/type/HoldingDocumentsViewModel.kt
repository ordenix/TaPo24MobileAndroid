package pl.tapo24.twa.ui.road.holdingDocuments.type

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.adapter.HoldingDocumentsAdapter
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.dbData.entity.HoldingDocuments
import javax.inject.Inject


@HiltViewModel
class HoldingDocumentsViewModel @Inject constructor(
    private val dataTapoDb: DataTapoDb
) : ViewModel() {
    val data =  MutableLiveData<List<HoldingDocuments>>()
    lateinit var adapter: HoldingDocumentsAdapter


     fun getData(type: String, otherCounty: Boolean) {
         viewModelScope.launch(Dispatchers.IO) {
             var dataFromDb: List<HoldingDocuments>? = null
             async { dataFromDb = dataTapoDb.holdingDocuments().getAllByTypeAndCountry(type, otherCounty) }.await()
             withContext(Dispatchers.Main) {
                 if (dataFromDb != null) {
                     data.value = dataFromDb!!
                 }
             }

         }
     }
}