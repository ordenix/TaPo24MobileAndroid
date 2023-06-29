package pl.tapo24.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel: ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Thisdsfsdf sdf df  is seting"
    }
    val text: LiveData<String> = _text
}