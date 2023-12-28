package pl.tapo24.twa.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import pl.tapo24.twa.useCase.customCategoryMap.SynchronizeCustomCategoryMapUseCase

class CustomCategoryMapSynchronizeWorker(val context: Context, params: WorkerParameters): CoroutineWorker(context,params) {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface SynchronizeCustomCustomCategoryMapProviderEntryPoint {
        fun synchronizeCustomCategoryMapUseCase(): SynchronizeCustomCategoryMapUseCase
    }

    override suspend fun doWork(): Result {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(context, SynchronizeCustomCustomCategoryMapProviderEntryPoint::class.java)
        val synchronizeCustomCategoryUseCase = hiltEntryPoint.synchronizeCustomCategoryMapUseCase()
        synchronizeCustomCategoryUseCase.synchronize()
        return Result.success()
    }
}