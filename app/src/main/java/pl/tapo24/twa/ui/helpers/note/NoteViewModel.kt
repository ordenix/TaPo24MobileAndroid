package pl.tapo24.twa.ui.helpers.note

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Setting
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val tapoDb: TapoDb
) : ViewModel() {
    val text: MutableLiveData<String> = MutableLiveData()
    val number: MutableLiveData<String> = MutableLiveData()


    fun saveNumber(number: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val settingNumberToDb= Setting("number_note",number)
            async { tapoDb.settingDb().insert(settingNumberToDb) }.await()
        }
    }

    fun saveText(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val settingStringToDb= Setting("string_note",text)
            async { tapoDb.settingDb().insert(settingStringToDb) }.await()
        }

    }

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            var settingStringFromDb: String? = null
            var settingNumberFromDb: String? = null
            async { settingStringFromDb = tapoDb.settingDb().getSettingByName("string_note")?.value }.await()
            async { settingNumberFromDb = tapoDb.settingDb().getSettingByName("number_note")?.value }.await()

            withContext(Dispatchers.Main) {
                if (settingStringFromDb?.isNotEmpty() == true) {
                    text.value = settingStringFromDb.toString()
                }
                if (settingNumberFromDb?.isNotEmpty() == true) {
                    number.value = settingNumberFromDb.toString()
                }
            }

        }

    }

}