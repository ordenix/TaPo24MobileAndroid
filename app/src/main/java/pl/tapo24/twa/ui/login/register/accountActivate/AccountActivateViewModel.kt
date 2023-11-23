package pl.tapo24.twa.ui.login.register.accountActivate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.data.Dbid
import pl.tapo24.twa.data.register.AccountTokenData
import pl.tapo24.twa.infrastructure.NetworkClientRegister
import pl.tapo24.twa.ui.login.register.ParseUrl
import javax.inject.Inject

@HiltViewModel
class AccountActivateViewModel @Inject constructor(
    private val networkClientRegister: NetworkClientRegister,
) : ViewModel() {
    private var tokenData = AccountTokenData(0,"")
    val status:MutableLiveData<Result<String>> = MutableLiveData()
    val statusCheckAccountActivate:MutableLiveData<Result<String>> = MutableLiveData()
    val statusRegenerateToken:MutableLiveData<Result<String>> = MutableLiveData()

    fun sendRequest() {
        var response: Result<String>? = null
        viewModelScope.launch(Dispatchers.IO) {
            async {
                response = networkClientRegister.accountActivate(tokenData)
            }.await()
            withContext(Dispatchers.Main) {
                if (response!=null) {
                    status.value = response!!
                }
            }
        }
    }

    fun checkAccountActivated() {
        var result: Result<String>? = null
        val dbid = Dbid(tokenData.dbid)
        viewModelScope.launch(Dispatchers.IO) {
            async { result = networkClientRegister.checkIsAccountActivated(dbid) }.await()
            withContext(Dispatchers.Main) {
                if (result!=null) {
                    statusCheckAccountActivate.value = result!!
                }
            }
        }
    }

    fun regenerateToken() {
        var result: Result<String>? = null
        val dbid = Dbid(tokenData.dbid)
        viewModelScope.launch(Dispatchers.IO) {
            async { result = networkClientRegister.resendActivationToken(dbid) }.await()
            withContext(Dispatchers.Main) {
                if (result!=null) {
                    statusRegenerateToken.value = result!!
                }
            }
        }
    }

    fun decodeUrl(url: String) {
        tokenData = ParseUrl().parse(url)!!
    }
}