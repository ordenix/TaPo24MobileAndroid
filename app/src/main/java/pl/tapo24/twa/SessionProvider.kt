package pl.tapo24.twa

import android.content.Context
import android.content.SharedPreferences
import androidx.work.*
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import org.acra.ACRA
import pl.tapo24.twa.data.NetworkTypes
import pl.tapo24.twa.data.State
import pl.tapo24.twa.data.login.DataUser
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Setting
import pl.tapo24.twa.infrastructure.NetworkClient
import pl.tapo24.twa.module.FavouriteModule
import pl.tapo24.twa.updater.LawUpdater
import pl.tapo24.twa.utils.CheckConnection
import pl.tapo24.twa.worker.RegenerateJwtTokenWorker
import pl.tapo24.twa.worker.ValidateTokenJwtWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SessionProvider @Inject constructor(private var tapoDb: TapoDb,
                                            private var networkClient: NetworkClient,
                                            @ApplicationContext private val context: Context,
                                            private val favouriteModule: FavouriteModule
)
{

    private lateinit var sharedPreferences: SharedPreferences

    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface LawUpdaterProviderEntryPoint {
        fun lawUpdater(): LawUpdater
    }


    fun createSession(jwtToken: String) {
        val workManager: WorkManager = WorkManager.getInstance(context)

/////
        sharedPreferences = context.getSharedPreferences(
            "Jwt",
            Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("token", jwtToken).apply()
        State.isLogin.value = true
        State.jwtToken = jwtToken


        MainScope().launch(Dispatchers.IO) {
            val settingToDb = Setting("jwtToken", jwtToken)
            async { tapoDb.settingDb().insert(settingToDb) }.await()
            // get data from backend
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
                val hiltEntryPoint = EntryPointAccessors.fromApplication(context,
                    LawUpdaterProviderEntryPoint::class.java)
                hiltEntryPoint.lawUpdater().update(jwt = jwtToken)
                favouriteModule.synchronizeOnSessionCreatedOrInternetAvailable()

            }
        }

        // add worker to regenerate token
        val workRequest = PeriodicWorkRequestBuilder<RegenerateJwtTokenWorker>(15, TimeUnit.DAYS)
            .setConstraints(constraints)
            .setInitialDelay(15, TimeUnit.DAYS)
            .build()
        workManager.enqueueUniquePeriodicWork("RegenerateJwt", ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE, workRequest)
    }

    // execute on start
    fun restoreSession() {
        val workManager: WorkManager = WorkManager.getInstance(context)

        MainScope().launch(Dispatchers.IO) {
            val jwtTokenFromDb = async{tapoDb.settingDb().getSettingByName("jwtToken") }.await()
            if (jwtTokenFromDb != null) {
                if  (CheckConnection().getConnectionType(context) != NetworkTypes.None) {
                    // internet available valid token
                    val checkToken  = networkClient.checkValidToken(jwtTokenFromDb!!.value)
                    checkToken.onSuccess {
                        // token ok user login and network available
                        loadDataFromDb()
                        favouriteModule.synchronizeOnSessionCreatedOrInternetAvailable()
                    }
                    checkToken.onFailure { ex->
                        withContext(Dispatchers.Main) {
                            clearSession()
                            ACRA.errorReporter.putCustomData("CLEAR SESSION AT ${System.currentTimeMillis()}", jwtTokenFromDb.value)
                            ACRA.errorReporter.handleSilentException(ex)
                        }
                    }
                } else {
                    // internet not available trust data in db
                    val workRequest = OneTimeWorkRequestBuilder<ValidateTokenJwtWorker>()
                        .setConstraints(constraints)
                        .build()
                    workManager.enqueueUniqueWork("CheckValidToken", ExistingWorkPolicy.REPLACE, workRequest)
                    loadDataFromDb()
                }
            }
        }
    }
    private fun loadDataFromDb() {
        MainScope().launch(Dispatchers.IO) {
            val jwtTokenFromDb = async{tapoDb.settingDb().getSettingByName("jwtToken") }.await()
            val userNameFromDb = async{tapoDb.settingDb().getSettingByName("UserName") }.await()
            val roleFromDb = async{tapoDb.settingDb().getSettingByName("Role") }.await()
            val betaFromDb = async{tapoDb.settingDb().getSettingByName("Beta") }.await()
            withContext(Dispatchers.Main) {
                State.jwtToken = jwtTokenFromDb!!.value
                State.premiumVersion = roleFromDb!!.value == "Admin" || roleFromDb.value == "Vip"
                State.isLogin.value = true
                State.userName = userNameFromDb!!.value
                State.paymentId.value = userNameFromDb.value
                State.isAdmin.value = roleFromDb.value == "admin"
                State.beta = betaFromDb!!.value == "true"
            }
        }
    }
    fun clearSession() {
        val workManager: WorkManager = WorkManager.getInstance(context)
        ACRA.errorReporter.handleSilentException(Exception("CLEAR SESSION FUNCTION AT ${System.currentTimeMillis()}"))
        workManager.cancelUniqueWork("RegenerateJwt")
        // clear data in state
        State.isLogin.value = false
        State.premiumVersion = false
        State.paymentId.value = ""
        State.premiumVersion = false
        State.userName = ""
        State.beta = false
        State.isAdmin.value = false
        // clearDb
        val settingToDbJwtToken = Setting("jwtToken")
        val settingToDbUserName = Setting("UserName")
        val settingToDbRole = Setting("Role")
        val settingToDbBetaStatus = Setting("Beta")
        MainScope().launch(Dispatchers.IO) {
            tapoDb.settingDb().deleteElement(settingToDbJwtToken)
            tapoDb.settingDb().deleteElement(settingToDbUserName)
            tapoDb.settingDb().deleteElement(settingToDbRole)
            tapoDb.settingDb().deleteElement(settingToDbBetaStatus)
        }
        // clear shared preferences
        sharedPreferences = context.getSharedPreferences(
            "Jwt",
            Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("token", null).apply()
    }
}