package pl.tapo24.twa.worker

import android.content.Context
import android.content.SharedPreferences
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import pl.tapo24.twa.updater.AssetUpdater

import pl.tapo24.twa.updater.LawUpdater

class UpdateWorker(val context: Context, val workerParameters: WorkerParameters): CoroutineWorker(context, workerParameters) {

    private lateinit var sharedPreferences: SharedPreferences


    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface LawUpdaterProviderEntryPoint {
        fun lawUpdater(): LawUpdater
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface AssetUpdaterProviderEntryPoint {
        fun assetUpdater(): AssetUpdater
    }


    override suspend fun doWork(): Result {
        sharedPreferences = context.getSharedPreferences(
            "Jwt",
            Context.MODE_PRIVATE
        )
        val jwt: String? = sharedPreferences.getString("token", null)
        val hiltEntryPoint = EntryPointAccessors.fromApplication(context,
            LawUpdaterProviderEntryPoint::class.java)
        hiltEntryPoint.lawUpdater().update(jwt = jwt)
        val hiltEntryPoint2 = EntryPointAccessors.fromApplication(context,
            AssetUpdaterProviderEntryPoint::class.java)
        hiltEntryPoint2.assetUpdater().update()
        return Result.success()
    }

}