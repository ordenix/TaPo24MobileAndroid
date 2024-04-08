package pl.tapo24.twa

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.work.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import pl.tapo24.twa.data.State
import pl.tapo24.twa.data.TokenDecodeData
import java.util.Base64
import kotlinx.serialization.decodeFromString
import pl.tapo24.twa.data.NetworkTypes
import pl.tapo24.twa.data.login.DataUser
import pl.tapo24.twa.data.login.ToLoginData
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Setting
import pl.tapo24.twa.exceptions.HttpException
import pl.tapo24.twa.exceptions.InternalException
import pl.tapo24.twa.exceptions.InternalMessage
import pl.tapo24.twa.infrastructure.NetworkClient
import pl.tapo24.twa.succes.HttpSuccessMessage
import pl.tapo24.twa.updater.LawUpdater
import pl.tapo24.twa.worker.RegenerateJwtTokenWorker
import pl.tapo24.twa.worker.UpdateWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SessionProvider @Inject constructor(private var tapoDb: TapoDb, private var networkClient: NetworkClient, private var context: Context,)

 {

     private lateinit var sharedPreferences: SharedPreferences

     val workManager: WorkManager = WorkManager.getInstance(context)
     val constraints = Constraints.Builder()
         .setRequiredNetworkType(NetworkType.CONNECTED)

         .build()

     @EntryPoint
     @InstallIn(SingletonComponent::class)
     interface LawUpdaterProviderEntryPoint {
         fun lawUpdater(): LawUpdater
     }

    fun restoreSession() {
        State.isSessionRestored = true
        MainScope().launch(Dispatchers.IO) {
            var jwtFromDb: Setting? = null
            async { jwtFromDb = tapoDb.settingDb().getSettingByName("jwtToken") }.await()
            if (jwtFromDb != null) {
                if(State.internetStatus.value != NetworkTypes.None) {
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

    private fun createSession(jwtToken: String, getOptionalData: Boolean = false) {
        sharedPreferences = context.getSharedPreferences(
            "Jwt",
            Context.MODE_PRIVATE)
        State.isLogin.value = true
        State.jwtToken = jwtToken
        sharedPreferences.edit().putString("token", jwtToken).apply()

        val settingToDb = Setting("jwtToken", jwtToken)
        MainScope().launch(Dispatchers.IO) {
            async { tapoDb.settingDb().insert(settingToDb) }.await()
        }
        if (State.internetStatus.value != NetworkTypes.None) {
            // get data from backend
            MainScope().launch(Dispatchers.IO) {
            val userDataFromBackend: Result<DataUser> = networkClient.getDataUser()
            userDataFromBackend.onSuccess {
                val settingToDbUserName = Setting("UserName", it.login!!)
                val settingToDbRole = Setting("Role", it.role!!)
                val settingToDbBetaStatus = Setting("Beta", state = it.isBetaTester ?: false)
                State.premiumVersion = it.role!! == "Admin" || it.role!! == "Vip"
                State.userName = it.login!!
                State.beta = it.isBetaTester?: false
                async { tapoDb.settingDb().insert(settingToDbUserName) }.await()
                async { tapoDb.settingDb().insert(settingToDbRole) }.await()
                async { tapoDb.settingDb().insert(settingToDbBetaStatus) }.await()
                withContext(Dispatchers.Main) {
                    State.paymentId.value = it.login!!
                    State.isAdmin.value = it.role!! == "Admin"
                }
                if (getOptionalData) {
                    val hiltEntryPoint = EntryPointAccessors.fromApplication(context,
                        LawUpdaterProviderEntryPoint::class.java)
                    hiltEntryPoint.lawUpdater().update(jwt = jwtToken)
                }

            }
        }
        } else {
            // offline
            var userNameFromDb: Setting? = null
            var roleFromDb: Setting? = null
            MainScope().launch(Dispatchers.IO) {
                async { userNameFromDb = tapoDb.settingDb().getSettingByName("UserName") }.await()
                async { roleFromDb = tapoDb.settingDb().getSettingByName("Role") }.await()
                val betaFromDb = async {tapoDb.settingDb().getSettingByName("Role") }.await()
                withContext(Dispatchers.Main) {
                    if (userNameFromDb != null) {
                        State.userName = userNameFromDb!!.value
                    }
                    if (roleFromDb != null) {
                        State.premiumVersion = roleFromDb!!.value == "Admin" || roleFromDb!!.value == "Vip"
                    }
                    if (betaFromDb != null) {
                        State.beta = betaFromDb.state
                    }
                }
            }
        }
    }
    fun loginToServiceViaGoogle(jwtToken: String) {
        MainScope().launch(Dispatchers.Main) {
            createSession(jwtToken, true)

        }
        val workRequest = PeriodicWorkRequestBuilder<RegenerateJwtTokenWorker>(15, TimeUnit.DAYS)
            .setConstraints(constraints)
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()
        workManager.enqueueUniquePeriodicWork("RegenerateJwt", ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE, workRequest)

    }
    suspend fun loginToService(dataToLogin: ToLoginData):Result<String> {
        var response: Result<String>? = null
        response = networkClient.login(dataToLogin)
        response.onSuccess {
            withContext(Dispatchers.Main) {
                createSession(it, true)
               // here add worker to regenrate
            val workRequest = PeriodicWorkRequestBuilder<RegenerateJwtTokenWorker>(15, TimeUnit.DAYS)
                    .setConstraints(constraints)
                    .setInitialDelay(15, TimeUnit.DAYS)
                    .build()
            workManager.enqueueUniquePeriodicWork("RegenerateJwt", ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE, workRequest)

            }

            return  Result.success(HttpSuccessMessage.SuccessLogin.message)
        }
        response.onFailure {
            return Result.failure(HttpException(it.message))
        }
        return Result.failure(InternalException(InternalMessage.InternalLogin.message))
    }

    fun clearSession() {
//        remove worker if exist
        workManager.cancelUniqueWork("RegenerateJwt")
        State.isLogin.value = false
        State.premiumVersion = false
        State.paymentId.value = ""
        // State.uid = "" // TODO: SET TATE UID AND DB BY LOGIN UID
        State.userName = ""
        val settingToDb = Setting("jwtToken",)
        MainScope().launch(Dispatchers.IO) {
            tapoDb.settingDb().deleteElement(settingToDb)

        }
        State.isAdmin.value = false
        sharedPreferences = context.getSharedPreferences(
            "Jwt",
            Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("token", null).apply()


    }
}