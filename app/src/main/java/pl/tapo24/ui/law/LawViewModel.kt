package pl.tapo24.ui.law

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LawViewModel: ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is LAW"
    }
    val text: LiveData<String> = _text
}