package pl.tapo24.twa.ui.login.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.data.register.RegisterForm
import pl.tapo24.twa.exceptions.InternalException
import pl.tapo24.twa.exceptions.InternalMessage
import pl.tapo24.twa.infrastructure.NetworkClient
import pl.tapo24.twa.infrastructure.NetworkClientRegister
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val networkClientRegister: NetworkClientRegister
): ViewModel() {
    val validateLogin: MutableLiveData<Result<String>> = MutableLiveData()
    val validateEmail: MutableLiveData<Result<String>> = MutableLiveData()
    val statusRegister: MutableLiveData<Result<String>> = MutableLiveData()
    fun existLoginInService(login: String) {
        var response: Result<String>? = null
        viewModelScope.launch(Dispatchers.IO) {

            async {
                val dataToSend = RegisterForm(false,false,"",login,"")
                response = networkClientRegister.checkLogin(dataToSend)
            }.await()
            withContext(Dispatchers.Main) {
                if (response != null) {
                    response!!.onSuccess {
                        validateLogin.value =  Result.success(it)
                    }
                    response!!.onFailure {
                        validateLogin.value =   Result.failure(it)
                    }
                }
            }
        }

    }

    fun existEmailInService(email: String) {
        var response: Result<String>? = null
        viewModelScope.launch(Dispatchers.IO) {

            async {
                val dataToSend = RegisterForm(false,false,email,"","")
                response = networkClientRegister.checkEmail(dataToSend)
            }.await()
            withContext(Dispatchers.Main) {
                if (response != null) {
                    response!!.onSuccess {
                        validateEmail.value =  Result.success(it)
                    }
                    response!!.onFailure {
                        validateEmail.value =   Result.failure(it)
                    }
                }
            }
        }
    }

    fun registerUser(data: RegisterForm) {
        viewModelScope.launch(Dispatchers.IO) {
            var response: Result<String>? = null
            async { response = networkClientRegister.registerUser(data) }.await()
            withContext(Dispatchers.Main) {
                if (response != null) {
                    response!!.onSuccess {
                        statusRegister.value =  Result.success(it)
                    }
                    response!!.onFailure {
                        statusRegister.value =   Result.failure(it)
                    }
                }
            }
        }
    }
}
