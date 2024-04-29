package pl.tapo24.twa.worker

import android.content.Context
import android.content.SharedPreferences
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.MainActivity
import pl.tapo24.twa.data.NetworkTypes
import pl.tapo24.twa.data.State
import pl.tapo24.twa.updater.MourningCheck
import pl.tapo24.twa.utils.CheckConnection
import javax.inject.Inject

//@EntryPoint
//@InstallIn(SingletonComponent::class)
class MourningWorker (val context: Context, val parameters: WorkerParameters): CoroutineWorker(context, parameters) {


    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface MourningProviderEntryPoint {
        fun mourningCheck(): MourningCheck
    }

    override suspend fun doWork(): Result {
        val networkType = CheckConnection().getConnectionType(context)
        val hiltEntryPoint = EntryPointAccessors.fromApplication(context, MourningProviderEntryPoint::class.java)
        val mourningCheck = hiltEntryPoint.mourningCheck()
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            "ThemePref",
            Context.MODE_PRIVATE
        )

        if (networkType != NetworkTypes.None) {
            mourningCheck.getMourningFromService()
            checkMourning(mourningCheck, sharedPreferences)
            return Result.success()
        } else {
            checkMourning(mourningCheck, sharedPreferences)
            return Result.success()
        }


    }
    private suspend fun checkMourning(mourningCheck: MourningCheck, sharedPreferences: SharedPreferences) {
        val funeralFromShared: Boolean = sharedPreferences.getBoolean("funeralTheme", false)
        val statusMourning = mourningCheck.checkIsMourningActive()
        statusMourning.onSuccess {
            if (it != funeralFromShared) {
                sharedPreferences.edit().putBoolean("funeralTheme", it).apply()
                withContext(Dispatchers.Main) {
                    State.requireRestart.value = true
                }
            }
        }
    }
}