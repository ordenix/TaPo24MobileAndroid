package pl.tapo24.twa

import android.content.Context
import android.os.Build
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import pl.tapo24.twa.data.State
import pl.tapo24.twa.data.TokenDecodeData
import java.util.Base64
import kotlinx.serialization.decodeFromString
import pl.tapo24.twa.data.login.ToLoginData
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Setting
import pl.tapo24.twa.exceptions.HttpException
import pl.tapo24.twa.exceptions.InternalException
import pl.tapo24.twa.exceptions.InternalMessage
import pl.tapo24.twa.infrastructure.NetworkClient
import pl.tapo24.twa.succes.HttpSuccessMessage
import javax.inject.Inject

class SessionProvider @Inject constructor(private var tapoDb: TapoDb, private var networkClient: NetworkClient)

 {


    fun restoreSession() {
        State.isSessionRestored = true
        MainScope().launch(Dispatchers.IO) {
            var jwtFromDb: Setting? = null
            async { jwtFromDb = tapoDb.settingDb().getSettingByName("jwtToken") }.await()
            if (jwtFromDb != null) {
                if(State.internetStatus.value !=0) {
                    var response: Result<String>? = null
                    response = networkClient.checkValidToken(jwtFromDb!!.value)
                    response.onSuccess {
                        withContext(Dispatchers.Main) {
                            State.isSessionConfirm = true
                            createSession(jwtFromDb!!.value)
                        }
                    }
                    response.onFailure {
                        withContext(Dispatchers.Main) {
                            clearSession()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        State.isSessionConfirm = false
                        createSession(jwtFromDb!!.value)
                    }
                }

            } else {
                withContext(Dispatchers.Main) {
                    clearSession()
                }
            }
        }

    }

    fun testValidSession() {

    }

    private fun createSession(jwtToken: String) {
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
                // TODO: MAT24-36 Wyszukiwarka listy kontrolnej
                // State.premiumVersion = true
            } else {
                // TODO: UNCOMENT ABOVE
                //State.premiumVersion = false
            }

        } else {
           // TODO("VERSION.SDK_INT < O AND ADD IN BACKEND FUNCTION THAT RETURN ROLE add to save it and check if internet is online")
        }


    }

    suspend fun loginToService(dataToLogin: ToLoginData):Result<String> {
        var response: Result<String>? = null
        response = networkClient.login(dataToLogin)
        response.onSuccess {
            withContext(Dispatchers.Main) {
                createSession(it)
            }

            return  Result.success(HttpSuccessMessage.SuccessLogin.message)
        }
        response.onFailure {
            return Result.failure(HttpException(it.message))
        }
        return Result.failure(InternalException(InternalMessage.InternalLogin.message))
    }

    fun clearSession() {
        State.isLogin.value = false
        State.premiumVersion = false
        // State.uid = "" // TODO: SET TATE UID AND DB BY LOGIN UID
        State.userName = ""
        val settingToDb = Setting("jwtToken",)
        MainScope().launch(Dispatchers.IO) {
            tapoDb.settingDb().deleteElement(settingToDb)

        }

    }
}