package pl.tapo24.twa.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.data.login.ToLoginData
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.infrastructure.NetworkClient
import pl.tapo24.twa.SessionProvider
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sessionProvider: SessionProvider
): ViewModel() {

    val showError: MutableLiveData<Boolean> = MutableLiveData(false)
    val successLogin: MutableLiveData<Boolean> = MutableLiveData(false)
    var errorMessage = ""
//    private val _text = MutableLiveData<String>().apply {
//        value = "This is LOGIN"
//    }
//    val text: LiveData<String> = _text

    fun login(loginData: ToLoginData) {
        viewModelScope.launch(Dispatchers.IO) {
            val statusLogin = sessionProvider.loginToService(loginData)

            statusLogin.onSuccess {
                withContext(Dispatchers.Main) {
                    successLogin.value = true
                }
            }
            statusLogin.onFailure {
                withContext(Dispatchers.Main) {
                    errorMessage = it.message.toString()
                    showError.value = true
                }
            }
        }




    }

}