package pl.tapo24.twa.worker

import android.content.Context
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
        val hiltEntryPoint = EntryPointAccessors.fromApplication(context,
            LawUpdaterProviderEntryPoint::class.java)
        hiltEntryPoint.lawUpdater().update()
        val hiltEntryPoint2 = EntryPointAccessors.fromApplication(context,
            AssetUpdaterProviderEntryPoint::class.java)
        hiltEntryPoint2.assetUpdater().update()
        return Result.success()
    }

}