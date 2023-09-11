package pl.tapo24.twa.ui.road.sign.signDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pl.tapo24.twa.data.EnginesType
import pl.tapo24.twa.data.State
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Tariff
import pl.tapo24.twa.dbData.DataTapoDb
import javax.inject.Inject

@HiltViewModel
class SignDetailsViewModel @Inject constructor(
    private val dataTapoDb: DataTapoDb,
    private val tapoDb: TapoDb
)  : ViewModel() {


    fun getTariffDetails(linkId: String) {
        val tariffDetails: Tariff? = null
        viewModelScope.launch(Dispatchers.IO) {
            if (State.enginesType == EnginesType.New) {
                // new
                async { tariffDetails =   }.await()
            } else {
                // old
            }
        }

    }
}