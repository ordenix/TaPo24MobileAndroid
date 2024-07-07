package pl.tapo24.twa.ui.settings.promoCode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.SessionProvider
import pl.tapo24.twa.infrastructure.NetworkClient
import pl.tapo24.twa.module.PremiumShopModule
import javax.inject.Inject


@HiltViewModel
class PromoCodeViewModel
    @Inject constructor(
        private val networkClient: NetworkClient,
        private val sessionProvider: SessionProvider
    )
    : ViewModel() {
    val showErrorDialog = MutableLiveData<Boolean>(false)
    val errorMessage = MutableLiveData<String>("")
    val buttonBlocked = MutableLiveData<Boolean>(false)
    val showSuccessDialog = MutableLiveData(false)


    fun enterPromoCode(promoCode: String) {
        buttonBlocked.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val response = async { networkClient.executePromotionCode(promoCode) }.await()
            response.onSuccess {
                withContext(Dispatchers.Main) {
                    sessionProvider.createSession(it)
                    showSuccessDialog.value = true
                    buttonBlocked.value = false
                }
            }
            response.onFailure {
                withContext(Dispatchers.Main) {
                    errorMessage.value = it.message
                    showErrorDialog.value = true
                    buttonBlocked.value = false
                }

            }
        }

    }


}