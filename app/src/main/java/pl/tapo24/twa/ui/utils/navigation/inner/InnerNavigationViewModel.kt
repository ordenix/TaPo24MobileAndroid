package pl.tapo24.twa.ui.utils.navigation.inner

import androidx.lifecycle.ViewModel
import pl.tapo24.twa.adapter.NavAdapter

class InnerNavigationViewModel : ViewModel() {

    lateinit var type: String
    lateinit var adapter: NavAdapter

}