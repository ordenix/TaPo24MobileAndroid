package pl.tapo24.twa.ui.road.immunity.immunityPersonList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.adapter.ImmunityPersonListAdapter
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.dbData.entity.Immunity
import javax.inject.Inject

@HiltViewModel
class ImmunityPersonListViewModel @Inject constructor(
    private val dataTapoDb: DataTapoDb
) : ViewModel() {
    lateinit var adapter: ImmunityPersonListAdapter
    val personList: MutableLiveData<List<Immunity>>  = MutableLiveData()
    val selectedPerson: MutableLiveData<Immunity> = MutableLiveData()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val dataFromDb = async{dataTapoDb.immunity().getAll()}.await()
            if (dataFromDb.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    personList.value = dataFromDb
                }
            }
        }

    }

    fun selectItem(person: Immunity) {
        selectedPerson.value = person
    }

}