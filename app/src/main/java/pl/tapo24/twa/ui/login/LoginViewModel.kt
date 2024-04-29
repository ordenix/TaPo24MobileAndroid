package pl.tapo24.twa.ui.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.SessionProvider
import pl.tapo24.twa.data.login.ToLoginData
import pl.tapo24.twa.infrastructure.NetworkClient
import pl.tapo24.twa.useCase.login.RequestGenerateJwtTokenFromBackendUseCase
import pl.tapo24.twa.useCase.login.RequestGoogleLoginReturnGoogleTokenUseCase
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sessionProvider: SessionProvider,
    private val requestGoogleLoginReturnGoogleTokenUseCase: RequestGoogleLoginReturnGoogleTokenUseCase,
    private val requestGenerateJwtTokenFromBackendUseCase: RequestGenerateJwtTokenFromBackendUseCase,
    private val networkClient: NetworkClient
): ViewModel() {

    val showError: MutableLiveData<Boolean> = MutableLiveData(false)
    val successLogin: MutableLiveData<Boolean> = MutableLiveData(false)
    var errorMessage = ""
    val showLoader: MutableLiveData<Boolean> = MutableLiveData(false)
    val procesRegister: MutableLiveData<Boolean> = MutableLiveData(false)
    var tokenGoogle: String? = null
//    private val _text = MutableLiveData<String>().apply {
//        value = "This is LOGIN"
//    }
//    val text: LiveData<String> = _text

    fun login(loginData: ToLoginData) {
        showLoader.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val response = async { networkClient.login(loginData) }.await()
            response.onSuccess {
                withContext(Dispatchers.Main) {
                    sessionProvider.createSession(it)
                    showLoader.value = false
                    successLogin.value = true
                }
            }
            response.onFailure {
                withContext(Dispatchers.Main) {
                    showLoader.value = false
                    errorMessage = it.message.toString()
                    showError.value = true
                }
            }
        }

    }

    fun loginViaGoogle(context: Context) {
        showLoader.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val googleIdUseCase = async{requestGoogleLoginReturnGoogleTokenUseCase.run(context)}.await()
            googleIdUseCase.onSuccess {googleToken ->
                // handle proper googleIdToken
                val requestJwtToken = async{requestGenerateJwtTokenFromBackendUseCase.run(googleToken)}.await()

                requestJwtToken?.onSuccess {
                    withContext(Dispatchers.Main) {
                        showLoader.value = false
                        successLogin.value = true
                        sessionProvider.createSession(it)
                    }
                }
                requestJwtToken?.onFailure {
                    withContext(Dispatchers.Main) {
                        showLoader.value = false

                    }
                    if (it.message == "404") {
                        // proces register
                        withContext(Dispatchers.Main) {
                            procesRegister.value = true
                            tokenGoogle = googleToken
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            errorMessage = it.message.toString()
                            showError.value = true
                        }
                    }


                }
            }
            googleIdUseCase.onFailure {
                withContext(Dispatchers.Main) {
                    showLoader.value = false
                    errorMessage = it.message.toString()
                    showError.value = true
                }
            }
        }
    }

}