package pl.tapo24.twa.module

import android.content.Context
import android.content.SharedPreferences
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.getCustomerInfoWith
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pl.tapo24.twa.SessionProvider
import pl.tapo24.twa.data.State
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Setting
import pl.tapo24.twa.infrastructure.NetworkClient
import pl.tapo24.twa.updater.LawUpdater
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

class PremiumShopModule @Inject constructor(private val tapoDb: TapoDb, private val networkClient: NetworkClient, private val context: Context) {
    private lateinit var sharedPreferences: SharedPreferences


    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface SessionProviderEntryPoint {
        fun sessionProvider(): SessionProvider
    }


    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface LawUpdaterEntryPoint {
        fun lawUpdater(): LawUpdater
    }

    fun checkPermissionOnInit() {
        Purchases.sharedInstance.getCustomerInfoWith({ error -> println(error) }, { customerInfo ->
            println(customerInfo)
            if (customerInfo.activeSubscriptions.contains("premium_tapo:podstawowy")) {
                val dateExp = customerInfo.allExpirationDatesByProduct["premium_tapo:podstawowy"].toString()
                // TO DO VERIFY DATE-LATENCY FROM RENEVUEcAT IS ABOUT 5 MIN
                if (!State.premiumVersion) {
                    // user have PREMIUM and need to update in backend
                    requestUpdatePermissionInBackendAndSetNewJWT()
                }

            }
        })
    }

    fun requestUpdatePermissionInBackendAndSetNewJWT() {
        sharedPreferences = context.getSharedPreferences(
            "Jwt",
            Context.MODE_PRIVATE)
        MainScope().launch(Dispatchers.IO) {
            val resposne = async { networkClient.promoteToPaidAccount() }.await()
            resposne.onSuccess {
                var jwtNewToDb: Setting = Setting("jwtToken", it, 0)
                State.jwtToken = it
                sharedPreferences.edit().putString("token", it).apply()
                async {tapoDb.settingDb().insert(jwtNewToDb) }.await()
                val hiltEntryPoint = EntryPointAccessors.fromApplication(context, SessionProviderEntryPoint::class.java)
                val sessionProvider = hiltEntryPoint.sessionProvider()
                sessionProvider.restoreSession()
                val lawUpdaterEntryPoint = EntryPointAccessors.fromApplication(context, LawUpdaterEntryPoint::class.java)
                val lawUpdaterProvider = lawUpdaterEntryPoint.lawUpdater()
                lawUpdaterProvider.update(jwt= it)
            }

        }

    }

}