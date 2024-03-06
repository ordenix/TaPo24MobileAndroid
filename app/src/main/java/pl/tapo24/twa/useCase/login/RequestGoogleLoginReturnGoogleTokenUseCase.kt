package pl.tapo24.twa.useCase.login

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import pl.tapo24.twa.data.EnvironmentType
import pl.tapo24.twa.data.State
import pl.tapo24.twa.infrastructure.NetworkClient
import javax.inject.Inject

class RequestGoogleLoginReturnGoogleTokenUseCase @Inject constructor(
    private val networkClient: NetworkClient
    ){

    private val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(
            if (State.environmentType == EnvironmentType.Master) {
                "268792039367-83f6i355niqtb0au1ronoa3i6vrkcfp3.apps.googleusercontent.com" // production
            } else {
                "268792039367-i42me62l5gln37jhefh7h688oncultv8.apps.googleusercontent.com" /// test

            }
        )

        //.setServerClientId("268792039367-cgmrund8iffrdpv062tkudtt1705l34o.apps.googleusercontent.com")
        .build()
    private val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()


    suspend fun run(context: Context): Result<String> {
        val credentialManager = CredentialManager.create(context)
        var resultToReturn: Result<String>? = null
        MainScope().async {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )
                val credential = result.credential
                when (credential) {
                    is CustomCredential -> {
                        println(credential)
                    }
                }
                val googleIdTokenC = GoogleIdTokenCredential
                    .createFrom(credential.data)
                val googleIdToken = googleIdTokenC.idToken
                resultToReturn= Result.success(googleIdToken)
            } catch (e: GetCredentialException) {
                resultToReturn = Result.failure(e)
            }
            catch (e: GoogleIdTokenParsingException) {
                resultToReturn = Result.failure(e)
            }
            catch (e: Exception) {
                resultToReturn = Result.failure(e)
            }

        }.await()
        if (resultToReturn != null) {
            return resultToReturn!!
        }
        return Result.failure(Throwable("Internal error. During proces request"))
    }
}