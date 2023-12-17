package pl.tapo24.twa.worker

import android.content.Context
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
import pl.tapo24.twa.SessionProvider
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


//    @Inject
//    lateinit var mourningCheck: MourningCheck
    override suspend fun doWork(): Result {
        val networkType = CheckConnection().getConnectionType(context)
        val hiltEntryPoint = EntryPointAccessors.fromApplication(context, MourningProviderEntryPoint::class.java)
        val mourningCheck = hiltEntryPoint.mourningCheck()
        if (networkType != NetworkTypes.None) {
            mourningCheck.getMourningFromService()
            val statusMourning = mourningCheck.checkIsMourningActive()
            statusMourning.onSuccess {
                withContext(Dispatchers.Main ) {
                    State.funeralTheme.value = it
                }
            }
            return Result.success()
        } else {
            val statusMourning = mourningCheck.checkIsMourningActive()
            statusMourning.onSuccess {
                withContext(Dispatchers.Main ) {
                    State.funeralTheme.value = it
                }
            }

            return Result.success()
        }


    }
}