package pl.tapo24.ui.tariff

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TariffViewModel: ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Tarrif"
    }
    val text: LiveData<String> = _text
}