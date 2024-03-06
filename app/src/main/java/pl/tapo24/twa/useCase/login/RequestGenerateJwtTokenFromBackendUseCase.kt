package pl.tapo24.twa.useCase.login

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import pl.tapo24.twa.infrastructure.NetworkClient
import javax.inject.Inject

class RequestGenerateJwtTokenFromBackendUseCase @Inject constructor(private val networkClient: NetworkClient){

    suspend fun run (googleToken: String): Result<String>? {
        var result: Result<String>? = null
        MainScope().async(Dispatchers.IO) {
            result = networkClient.loginViaGoogle(googleToken)

        }.await()
        return result

    }
}