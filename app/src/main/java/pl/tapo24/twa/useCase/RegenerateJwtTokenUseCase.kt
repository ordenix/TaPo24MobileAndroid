package pl.tapo24.twa.useCase

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import pl.tapo24.twa.data.State
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.infrastructure.NetworkClient
import javax.inject.Inject

class RegenerateJwtTokenUseCase @Inject constructor(private val  networkClient: NetworkClient,
    private val tapoDb: TapoDb,
    @ApplicationContext private val context: Context) {
    private lateinit var sharedPreferences: SharedPreferences


    fun run() {
        sharedPreferences = context.getSharedPreferences(
            "Jwt",
            Context.MODE_PRIVATE
        )
        val jwtFromDb = tapoDb.settingDb().getSettingByName("jwtToken")
        if (jwtFromDb != null) {
            MainScope().launch(Dispatchers.IO) {
                val response = async {networkClient.generateNewToken(jwtFromDb.value)}.await()
                response.onSuccess {
                    jwtFromDb.value = it
                    tapoDb.settingDb().insert(jwtFromDb)
                    State.jwtToken = it
                    sharedPreferences.edit().putString("token", it).apply()

                }
            }

        }

    }
}