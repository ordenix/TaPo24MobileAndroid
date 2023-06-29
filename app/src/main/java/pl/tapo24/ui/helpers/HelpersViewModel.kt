package pl.tapo24.ui.helpers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HelpersViewModel: ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is HELPERS"
    }
    val text: LiveData<String> = _text
}