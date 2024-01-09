package pl.tapo24.twa.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import pl.tapo24.twa.useCase.RegenerateJwtTokenUseCase

class RegenerateJwtTokenWorker (val context: Context, val parameters: WorkerParameters): CoroutineWorker(context, parameters) {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface RegenerateJwtTokenProviderEntryPoint {
        fun regenerateJwtTokenUseCase(): RegenerateJwtTokenUseCase
    }

    override suspend fun doWork(): Result {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(context, RegenerateJwtTokenProviderEntryPoint::class.java)
        val regenerateJwtTokenUseCase = hiltEntryPoint.regenerateJwtTokenUseCase()
        regenerateJwtTokenUseCase.run()
        return Result.success()
    }


}