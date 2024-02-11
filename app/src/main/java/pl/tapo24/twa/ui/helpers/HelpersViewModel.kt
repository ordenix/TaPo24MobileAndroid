package pl.tapo24.twa.ui.helpers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.tapo24.twa.adapter.NavAdapter

class HelpersViewModel: ViewModel() {
    lateinit var adapter: NavAdapter
    private val _text = MutableLiveData<String>().apply {
        value = "This is HELPERS"
    }
    val text: LiveData<String> = _text
}