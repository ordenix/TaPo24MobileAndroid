package pl.tapo24.twa.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import pl.tapo24.twa.useCase.customCategory.SynchronizeCustomCategoryUseCase

class CustomCategorySynchronizeWorker(val context: Context,params: WorkerParameters): CoroutineWorker(context,params) {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface SynchronizeCustomCustomCategoryProviderEntryPoint {
        fun synchronizeCustomCategoryUseCase(): SynchronizeCustomCategoryUseCase
    }

    override suspend fun doWork(): Result {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(context, SynchronizeCustomCustomCategoryProviderEntryPoint::class.java)
        val synchronizeCustomCategoryUseCase = hiltEntryPoint.synchronizeCustomCategoryUseCase()
        synchronizeCustomCategoryUseCase.synchronize()
        return Result.success()

    }

}