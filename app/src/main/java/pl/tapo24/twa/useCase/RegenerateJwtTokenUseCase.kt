package pl.tapo24.twa.useCase

import kotlinx.coroutines.*
import pl.tapo24.twa.data.State
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.infrastructure.NetworkClient
import javax.inject.Inject

class RegenerateJwtTokenUseCase @Inject constructor(private val  networkClient: NetworkClient,
    private val tapoDb: TapoDb) {

    fun run() {
        val jwtFromDb = tapoDb.settingDb().getSettingByName("jwtToken")
        if (jwtFromDb != null) {
            MainScope().launch(Dispatchers.IO) {
                val response = async {networkClient.generateNewToken(jwtFromDb.value)}.await()
                response.onSuccess {
                    jwtFromDb.value = it
                    tapoDb.settingDb().insert(jwtFromDb)
                    State.jwtToken = it

                }
            }

        }

    }
}