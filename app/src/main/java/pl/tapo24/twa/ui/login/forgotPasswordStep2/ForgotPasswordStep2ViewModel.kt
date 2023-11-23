package pl.tapo24.twa.ui.login.forgotPasswordStep2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.data.register.AccountTokenData
import pl.tapo24.twa.data.register.TokenPasswordData
import pl.tapo24.twa.infrastructure.NetworkClientRegister
import pl.tapo24.twa.ui.login.register.ParseUrl
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordStep2ViewModel @Inject constructor(
    private val networkClientRegister: NetworkClientRegister
): ViewModel() {
    private var tokenData = AccountTokenData(0,"")
    val responseStatus: MutableLiveData<Result<String>> = MutableLiveData()


    fun sendRequest(password: String) {
        var result: Result<String>? = null
        val tokenPasswordData = TokenPasswordData(tokenData.token, password)
        viewModelScope.launch(Dispatchers.IO) {
            async { result = networkClientRegister.forgotPasswordStep2(tokenPasswordData) }.await()
            withContext(Dispatchers.Main) {
                if (result!=null) {
                    responseStatus.value = result!!
                }
            }
        }
    }

    fun decodeUrl(url: String) {
        tokenData = ParseUrl().parse(url)!!
    }

}