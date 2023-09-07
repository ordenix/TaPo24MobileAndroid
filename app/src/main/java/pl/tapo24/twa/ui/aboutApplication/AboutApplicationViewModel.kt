package pl.tapo24.twa.ui.aboutApplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AboutApplicationViewModel: ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is ABOUT APLICATION"
    }
    val text: LiveData<String> = _text
}