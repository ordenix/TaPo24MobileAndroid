package pl.tapo24.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is LOGIN"
    }
    val text: LiveData<String> = _text
}