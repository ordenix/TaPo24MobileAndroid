package pl.tapo24.twa.ui.helpers.postalCode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.data.postal.ResponseCity
import pl.tapo24.twa.infrastructure.NetworkClient

import javax.inject.Inject

@HiltViewModel
class PostalCodeViewModel @Inject constructor(
    private val networkClient: NetworkClient
): ViewModel() {
    val busy: MutableLiveData<Boolean> = MutableLiveData(false)
    val showError: MutableLiveData<Boolean> = MutableLiveData(false)
    var errorMessage: String = ""

    val responseCity: MutableLiveData<ResponseCity> = MutableLiveData()


    fun getCodeSequenceByCity(city: String) {
        busy.value = true
        viewModelScope.launch(Dispatchers.IO) {

        }

    }

    fun getCityByCode(code: String) {
        var codeToSend = code
        busy.value = true
        if (!codeToSend.contains('-')) {
            codeToSend =  codeToSend.replaceRange(2,2,"-")
        }
        viewModelScope.launch(Dispatchers.IO) {
            var responseList: ResponseCity? = null
            async {
                val response = networkClient.getPostalCityByCode(codeToSend)
                response.onSuccess {
                    responseList = it
                }
                response.onFailure {
                    withContext(Dispatchers.Main) {
                        errorMessage = it.message.toString()
                        showError.value = true
                        busy.value = false
                    }

                }
            }.await()
            withContext(Dispatchers.Main) {
                if (responseList != null)
                responseCity.value = responseList!!
                busy.value = false
            }
        }
    }

    fun validCode(code: String): Boolean {
        val regex = Regex(pattern = "\\d\\d-\\d\\d\\d|\\d\\d\\d\\d\\d", options = setOf(RegexOption.IGNORE_CASE))
        return regex.matches(code)

    }

}