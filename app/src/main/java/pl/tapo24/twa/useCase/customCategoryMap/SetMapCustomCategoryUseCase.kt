package pl.tapo24.twa.useCase.customCategoryMap

import android.content.Context
import androidx.work.*
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import pl.tapo24.twa.data.NetworkTypes
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.MapCategory
import pl.tapo24.twa.module.CustomCategoryModule
import pl.tapo24.twa.useCase.customCategory.SynchronizeCustomCategoryUseCase
import pl.tapo24.twa.utils.CheckConnection
import pl.tapo24.twa.worker.CustomCategoryMapSynchronizeWorker
import pl.tapo24.twa.worker.CustomCategorySynchronizeWorker
import javax.inject.Inject

class SetMapCustomCategoryUseCase @Inject constructor(
    private val context: Context,
    private val tapoDb: TapoDb
) {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface CustomCategoryProviderEntryPoint {
        fun customCategoryModule(): CustomCategoryModule
    }

    suspend fun runMap(tariffId: String, categoryId: Int) {
        var mapFromDb: MapCategory? = null
        var ids = 0
        MainScope().async(Dispatchers.IO) {
            mapFromDb = async { tapoDb.mapCategory().getByTariffIdAndCategoryId(tariffId, categoryId) }.await()
            ids = async { tapoDb.mapCategory().getCountOfElements() }.await()
        }.await()
        if (mapFromDb == null) {
            // not exist

            mapFromDb = MapCategory(
                id = ids,
                tariffId = tariffId,
                customCategoryId = categoryId
            )
            sendMap(mapFromDb!!)
        } else {
            // exist
            if (mapFromDb!!.toDelete) {
                // exist and is to delete
                mapFromDb!!.toDelete = false
                tapoDb.mapCategory().insert(mapFromDb!!)
            } else {
                // exist and is not to delete
                mapFromDb!!.toDelete = true
                sendMap(mapFromDb!!)
            }
        }

    }
    private suspend fun sendMap(mapCategory: MapCategory): Result<String> {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(context, CustomCategoryProviderEntryPoint::class.java)
        val customCategoryModule = hiltEntryPoint.customCategoryModule()
        if (CheckConnection().getConnectionType(context) != NetworkTypes.None) {
            if (mapCategory.toDelete) {
             // To delete
                val response = customCategoryModule.deleteMapCategory(mapCategory)
                response.onSuccess {
                    tapoDb.mapCategory().delete(mapCategory)
                }
                return response
            } else {
                val response = customCategoryModule.putMapCategory(mapCategory)
                response.onSuccess {
                    tapoDb.mapCategory().insert(mapCategory)
                }
                return response
            }
        } else {
            mapCategory.apply {
                dataSynchronized = false
            }
            MainScope().async(Dispatchers.IO) {
                async { tapoDb.mapCategory().insert(mapCategory) }.await()
            }.await()
            val workManager = WorkManager.getInstance(context)
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val workRequest = OneTimeWorkRequestBuilder<CustomCategoryMapSynchronizeWorker>()
                .setConstraints(constraints)
                .build()
            workManager.enqueueUniqueWork("CustomCategoryMapSynchronizeWorker", ExistingWorkPolicy.KEEP, workRequest)
            return Result.success("Ok")

        }
    }
}