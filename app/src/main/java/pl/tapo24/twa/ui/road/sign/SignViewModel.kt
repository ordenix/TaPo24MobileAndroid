package pl.tapo24.twa.ui.road.sign

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.tapo24.twa.db.TapoDb
import javax.inject.Inject

@HiltViewModel
class SignViewModel @Inject constructor (
    private val tapoDb: TapoDb
): ViewModel() {
    var isPublicStorage = false


    init {
        viewModelScope.launch(Dispatchers.IO) {
            isPublicStorage = tapoDb.settingDb().getSettingByName("publicStorage")?.state ?: false

        }
    }
}