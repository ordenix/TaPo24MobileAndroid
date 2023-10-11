package pl.tapo24.twa.ui.road.recidivism

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.adapter.TariffDataAdapter
import pl.tapo24.twa.data.EnginesType
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Tariff
import javax.inject.Inject

@HiltViewModel
class RecidivismViewModel @Inject constructor(
    private val tapoDb: TapoDb) : ViewModel() {

        val data: MutableLiveData<List<Tariff>> = MutableLiveData()
        lateinit var adapter: TariffDataAdapter

        fun getData() {
            viewModelScope.launch(Dispatchers.IO) {
                var dataFromDb:List<Tariff> = listOf()
                async { dataFromDb = tapoDb.tariffDb().getAllRecidivism() }.await()

                withContext(Dispatchers.Main) {
                    if (dataFromDb.isNotEmpty()) {
                        data.value = dataFromDb
                    }
                }

            }
        }

}