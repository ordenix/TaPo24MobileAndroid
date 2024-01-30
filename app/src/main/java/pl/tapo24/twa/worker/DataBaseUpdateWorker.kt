package pl.tapo24.twa.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import pl.tapo24.twa.updater.DataBaseUpdater

class DataBaseUpdateWorker (val context: Context, val parameters: WorkerParameters): CoroutineWorker(context, parameters) {
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface DataBaseUpdateProviderEntryPoint {
        fun dataBaseUpdater(): DataBaseUpdater
    }

    override suspend fun doWork(): Result {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(context,DataBaseUpdateProviderEntryPoint::class.java)
        hiltEntryPoint.dataBaseUpdater().update()

        return Result.success()
    }
}