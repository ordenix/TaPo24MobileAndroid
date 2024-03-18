package pl.tapo24.twa.ui.helpers.adrTable

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.dbData.DataTapoDb
import javax.inject.Inject

@HiltViewModel
class AdrTableViewModel @Inject constructor(
    private val dataTapoDb: DataTapoDb
): ViewModel() {
    val hinNumber: MutableLiveData<String> = MutableLiveData()
    val adrNumber: MutableLiveData<String> = MutableLiveData()

    fun getHinNumber(hinNumberToDb: String) {
        viewModelScope.launch(Dispatchers.IO) {

            val hinNumberFromDb = async { dataTapoDb.hinNumber().getByHinNumber(hinNumberToDb) }.await()
            if (hinNumberFromDb != null) {
                withContext(Dispatchers.Main) {
                    hinNumber.value = hinNumberFromDb.description!!
                }
            } else {
                withContext(Dispatchers.Main) {
                    hinNumber.value = "Brak kodu w bazie"
                }
            }
        }

    }

    fun getAdrNumber(adrNumberToDb: String) {
        viewModelScope.launch(Dispatchers.IO) {

            val adrNumberFromDb = async { dataTapoDb.adrNumber().getByAdrNumber(adrNumberToDb) }.await()
            if (adrNumberFromDb != null) {
                withContext(Dispatchers.Main) {
                    adrNumber.value = adrNumberFromDb.description!!
                }
            } else {
                withContext(Dispatchers.Main) {
                    adrNumber.value = "Brak kodu w bazie"
                }
            }
        }

    }




}