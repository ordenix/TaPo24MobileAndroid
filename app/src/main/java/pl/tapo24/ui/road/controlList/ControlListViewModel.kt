package pl.tapo24.ui.road.controlList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.adapter.ControlListAdapter
import pl.tapo24.dbData.DataTapoDb
import pl.tapo24.dbData.entity.ControlList
import javax.inject.Inject

@HiltViewModel
class ControlListViewModel @Inject constructor(
    private val dataTapoDb: DataTapoDb
): ViewModel() {
    val data = MutableLiveData<List<ControlList>>()
    lateinit var adapter:ControlListAdapter

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            var dataFromDb: List<ControlList>? = null
            async{dataFromDb = dataTapoDb.controlList().getAll()}.await()
            withContext(Dispatchers.Main) {
                if (dataFromDb != null) {
                    data.value = dataFromDb!!
                }
            }
        }

    }

    fun standardClicked() {

    }

    fun isVisible(point: String, item: ControlList): Boolean {
        var isVisible = false
        when (point) {
            "B" -> {
                if (item.standardLevelFaultsB1 != null || item.standardLevelFaultsB2 != null || (item.standardLevelFaultsB3 != null)) {
                    isVisible = true
                }
            }
            "C" -> {
                if (item.standardLevelFaultsC1 != null || item.standardLevelFaultsC2 != null || (item.standardLevelFaultsC3 != null)) {
                    isVisible = true
                }
            }
            "D" -> {
                if (item.standardLevelFaultsD1 != null || item.standardLevelFaultsD2 != null || (item.standardLevelFaultsD3 != null)) {
                    isVisible = true
                }
            }
            "E" -> {
                if (item.standardLevelFaultsE1 != null || item.standardLevelFaultsE2 != null || (item.standardLevelFaultsE3 != null)) {
                    isVisible = true
                }
            }
            "F" -> {
                if (item.standardLevelFaultsF1 != null || item.standardLevelFaultsF2 != null || (item.standardLevelFaultsF3 != null)) {
                    isVisible = true
                }
            }
            "G" -> {
                if (item.standardLevelFaultsG1 != null || item.standardLevelFaultsG2 != null || (item.standardLevelFaultsG3 != null)) {
                    isVisible = true
                }
            }
            "H" -> {
                if (item.standardLevelFaultsH1 != null || item.standardLevelFaultsH2 != null || (item.standardLevelFaultsH3 != null)) {
                    isVisible = true
                }
            }
            "I" -> {
                if (item.standardLevelFaultsI1 != null || item.standardLevelFaultsI2 != null || (item.standardLevelFaultsI3 != null)) {
                    isVisible = true
                }
            }
            "J" -> {
                if (item.standardLevelFaultsJ1 != null || item.standardLevelFaultsJ2 != null || (item.standardLevelFaultsJ3 != null)) {
                    isVisible = true
                }
            }
        }
        return isVisible
    }
}