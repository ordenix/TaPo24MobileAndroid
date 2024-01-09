package pl.tapo24.twa.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import pl.tapo24.twa.useCase.ShowNotifyForTariffIconUseCase

class ShowNotifyForTariffIconWorker  (val context: Context, val parameters: WorkerParameters): CoroutineWorker(context, parameters){


    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface ShowNotifyForTariffIconProviderEntryPoint {
        fun showNotifyForTariffIconUseCase(): ShowNotifyForTariffIconUseCase
    }

    override suspend fun doWork(): Result {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(context, ShowNotifyForTariffIconProviderEntryPoint::class.java)
        val showNotifyForTariffIconUseCase = hiltEntryPoint.showNotifyForTariffIconUseCase()
        showNotifyForTariffIconUseCase.run()
        return Result.success()
    }
}