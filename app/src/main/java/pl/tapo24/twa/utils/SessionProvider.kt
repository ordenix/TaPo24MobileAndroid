package pl.tapo24.twa.utils

import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import pl.tapo24.twa.data.State
import pl.tapo24.twa.data.TokenDecodeData
import java.util.Base64
import kotlinx.serialization.decodeFromString
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Setting
import pl.tapo24.twa.infrastructure.NetworkClient

class SessionProvider(

    val tapoDb: TapoDb,
    val networkClient: NetworkClient
) {

    fun renewSession() {

    }

    fun createSession(jwtToken: String) {
        State.isLogin.value = true
        State.jwtToken = jwtToken
        val settingToDb = Setting("jwtToken", jwtToken)
        MainScope().launch(Dispatchers.IO) {
            async { tapoDb.settingDb().insert(settingToDb) }.await()
        }
        val arrayToken = jwtToken.split(".")
        var decodedJwt = ""
        var decodedData = TokenDecodeData()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            decodedJwt = Base64.getUrlDecoder().decode(arrayToken[1]).decodeToString()
            decodedData = Json.decodeFromString(decodedJwt)
            State.userName = decodedData.sub.toString()
            if (decodedData.role == "Admin" || decodedData.role ==  "Vip") {
                State.premiumVersion = true
            } else {
                // TODO UNCOMENT ABOVE
                //State.premiumVersion = false
            }

        } else {
            TODO("VERSION.SDK_INT < O AND ADD IN BACKEND FUNCTION THAT RETURN ROLE")
        }


    }

    fun clearSession() {
        State.isLogin.value = false
        State.premiumVersion = false
        State.uid = ""
        State.userName = ""
        val settingToDb = Setting("jwtToken",)
        tapoDb.settingDb().deleteElement(settingToDb)

    }
}