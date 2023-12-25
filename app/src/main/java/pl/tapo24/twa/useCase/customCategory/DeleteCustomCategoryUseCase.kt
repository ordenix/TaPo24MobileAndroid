package pl.tapo24.twa.useCase.customCategory

import android.content.Context
import androidx.work.*
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pl.tapo24.twa.R
import pl.tapo24.twa.data.NetworkTypes
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.CustomCategory
import pl.tapo24.twa.exceptions.InternalException
import pl.tapo24.twa.module.CustomCategoryModule
import pl.tapo24.twa.utils.CheckConnection
import pl.tapo24.twa.worker.CustomCategorySynchronizeWorker
import javax.inject.Inject

class DeleteCustomCategoryUseCase @Inject constructor(
    private val context: Context,
    private val tapoDb: TapoDb

    ){

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface CustomCategoryProviderEntryPoint {
        fun customCategoryModule(): CustomCategoryModule
    }


    suspend fun run(customCategory: CustomCategory): Result<String> {
        if (CheckConnection().getConnectionType(context) != NetworkTypes.None) {
            val hiltEntryPoint = EntryPointAccessors.fromApplication(context, CustomCategoryProviderEntryPoint::class.java)
            val customCategoryModule = hiltEntryPoint.customCategoryModule()
            var response: Result<String>? = null
            MainScope().async(Dispatchers.IO) {
                response = customCategoryModule.deleteCustomCategory(customCategory)
            }.await()
            response!!.onSuccess {
                MainScope().async(Dispatchers.IO) {
                    async { tapoDb.customCategory().delete(customCategory) }.await()
                }.await()
                return Result.success(context.getString(R.string.deleteCustomCategory))
            }
            response!!.onFailure {
                return Result.failure(InternalException(it.message))
            }
        } else {
            customCategory.apply {
                toDelete = true
                // because we don't want to show deleted category and we want to place it at the end of the list
                sortOrder = 1000
            }
            MainScope().async(Dispatchers.IO) {
                async { tapoDb.customCategory().insert(customCategory) }.await()
            }.await()
            val workManager = WorkManager.getInstance(context)
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val workRequest = OneTimeWorkRequestBuilder<CustomCategorySynchronizeWorker>()
                .setConstraints(constraints)
                .build()
            workManager.enqueueUniqueWork("CustomCategorySynchronizeWorker", ExistingWorkPolicy.KEEP, workRequest)
            return Result.success(context.getString(R.string.deleteCustomCategory))

        }
        return Result.failure(InternalException("Unknown error at DeleteCustomCategoryUseCase"))
    }
}