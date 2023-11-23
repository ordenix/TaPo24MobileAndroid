package pl.tapo24.twa.ui.login.forgotPasswordStep1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.data.login.ToLoginData
import pl.tapo24.twa.infrastructure.NetworkClientRegister
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordStep1ViewModel @Inject constructor(
    private val networkClientRegister: NetworkClientRegister
) : ViewModel() {
    val responseStatus: MutableLiveData<Result<String>> = MutableLiveData()

    fun sendRequest(login: String) {
        val data: ToLoginData = ToLoginData(login,"")
        var result: Result<String>? = null
        viewModelScope.launch(Dispatchers.IO) {
            async { result = networkClientRegister.forgotPasswordStep1(data) }.await()
            withContext(Dispatchers.Main) {
                if (result!=null) {
                    responseStatus.value = result!!
                }
            }
        }
    }

}