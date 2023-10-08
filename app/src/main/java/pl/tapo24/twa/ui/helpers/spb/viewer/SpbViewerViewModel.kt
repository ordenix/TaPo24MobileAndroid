package pl.tapo24.twa.ui.helpers.spb.viewer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.dbData.entity.Spb
import javax.inject.Inject

@HiltViewModel
class SpbViewerViewModel @Inject constructor(
    private val dataTapoDb: DataTapoDb
) : ViewModel() {
    val data: MutableLiveData<Spb> = MutableLiveData()

    fun getData(type: String) {
        var listFromDb: Spb? = null

        viewModelScope.launch(Dispatchers.IO) {
            async {listFromDb = dataTapoDb.spb().getSpbByType(type)}.await()

            withContext(Dispatchers.Main) {
                if (listFromDb != null) {
                    data.value = listFromDb!!
                }
            }
        }
    }


}