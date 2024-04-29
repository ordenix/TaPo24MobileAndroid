package pl.tapo24.twa.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import pl.tapo24.twa.SessionProvider

class ValidateTokenJwtWorker(val context: Context, val workerParameters: WorkerParameters): CoroutineWorker(context, workerParameters) {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface SessionProviderEntryPoint {
        fun sessionProvider(): SessionProvider
    }

    override suspend fun doWork(): Result {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(context, SessionProviderEntryPoint::class.java)
        val sessionProvider = hiltEntryPoint.sessionProvider()
        sessionProvider.restoreSession()
        return Result.success()
    }
}