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
import pl.tapo24.twa.data.NetworkTypes
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.CustomCategory
import pl.tapo24.twa.exceptions.InternalException
import pl.tapo24.twa.module.CustomCategoryModule
import pl.tapo24.twa.utils.CheckConnection
import pl.tapo24.twa.worker.CustomCategorySynchronizeWorker
import javax.inject.Inject

class ChangeOrderCustomCategoryUseCase @Inject constructor(
    private val context: Context,
    private val tapoDb: TapoDb

) {


    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface CustomCategoryProviderEntryPoint {
        fun customCategoryModule(): CustomCategoryModule
    }

    suspend fun run(customCategoryList: List<CustomCategory>): Result<String> {
        customCategoryList.forEachIndexed { index, customCategory ->
            customCategory.apply {
                sortOrder = index
            }
        }
        if (CheckConnection().getConnectionType(context) != NetworkTypes.None) {
            val hiltEntryPoint = EntryPointAccessors.fromApplication(context, CustomCategoryProviderEntryPoint::class.java)
            val customCategoryModule = hiltEntryPoint.customCategoryModule()
            var response: Result<String>? = null
            MainScope().async(Dispatchers.IO) {
                response = customCategoryModule.putCustomCategory(customCategoryList)
            }.await()
            response!!.onSuccess {
                MainScope().async(Dispatchers.IO) {
                    async { tapoDb.customCategory().insertAll(customCategoryList) }.await()
                }.await()
                return Result.success(context.getString(pl.tapo24.twa.R.string.updateCustomCategory))
            }
            response!!.onFailure {
                return Result.failure(InternalException(it.message))
            }
        } else {
            customCategoryList.forEach { customCategory ->
                customCategory.apply {
                    dataSynchronized = false
                }
            }
            MainScope().async(Dispatchers.IO) {
                async { tapoDb.customCategory().insertAll(customCategoryList) }.await()
            }.await()
            val workManager = WorkManager.getInstance(context)
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val workRequest = OneTimeWorkRequestBuilder<CustomCategorySynchronizeWorker>()
                .setConstraints(constraints)
                .build()
            workManager.enqueueUniqueWork("CustomCategorySynchronizeWorker", ExistingWorkPolicy.KEEP, workRequest)
            return Result.success(context.getString(pl.tapo24.twa.R.string.updateCustomCategory))
        }
        return Result.failure(InternalException("Unknown error at ChangeOrderCustomCategoryUseCase"))

    }


}