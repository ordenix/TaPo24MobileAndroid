package pl.tapo24.twa.ui.road.technicalCondition.noiseLevel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.adapter.NoiseLevelAdapter
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.dbData.entity.NoiseLevel
import javax.inject.Inject

@HiltViewModel
class NoiseLevelViewModel @Inject constructor(
    private val dataTapoDb: DataTapoDb
): ViewModel() {
    lateinit var adapter: NoiseLevelAdapter

    val data: MutableLiveData<List<NoiseLevel>> = MutableLiveData()



    init {
        getData()
    }

    fun getData() {
    viewModelScope.launch(Dispatchers.IO) {
        val noiseList = async{dataTapoDb.noiseLevel().getAll()}.await()
        withContext(Dispatchers.Main) {
            data.value = noiseList
        }

    }

    }
}