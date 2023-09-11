package pl.tapo24.twa.ui.road.sign.signDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    val tariffDetail = MutableLiveData<Tariff?>()

    fun getTariffDetails(linkId: String) {
        var tariffDetails: Tariff? = null
        viewModelScope.launch(Dispatchers.IO) {
            if (State.enginesType == EnginesType.New) {
                // new
                async { tariffDetails = tapoDb.tariffDb().getByDocId(linkId)  }.await()
            } else {
                // old
                async { tariffDetails = tapoDb.tariffDb().getBySortOrderOld(linkId.toInt())  }.await()

            }
            withContext(Dispatchers.Main) {
                tariffDetail.value = tariffDetails
            }


        }

    }
}