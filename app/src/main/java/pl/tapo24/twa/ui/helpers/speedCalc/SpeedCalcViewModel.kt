package pl.tapo24.twa.ui.helpers.speedCalc

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
class SpeedCalcViewModel @Inject constructor(
    private val tapoDb: TapoDb
) : ViewModel() {
    val tariffDetail: MutableLiveData<Tariff> = MutableLiveData()
    fun calculateTariffForSpeedOvf(speedOverflow: Int) {
        var tariffDetails: Tariff? = null
        viewModelScope.launch(Dispatchers.IO) {
            if (State.enginesType == EnginesType.New) {
                // new
                async { tariffDetails = tapoDb.tariffDb().getBySpeedAndEngine(speedOverflow,"New")  }.await()
            } else {
                // old
                async { tariffDetails = tapoDb.tariffDb().getBySpeedAndEngine(speedOverflow,"Old")  }.await()

            }
            withContext(Dispatchers.Main) {
                if (tariffDetails!= null) {
                    tariffDetail.value = tariffDetails!!
                }

            }


        }
    }
    fun clickOnFavorites(item: Tariff) {
        item.favorites = !item.favorites
        viewModelScope.launch(Dispatchers.IO) {
            async {tapoDb.tariffDb().insert(item) }
        }
    }
}