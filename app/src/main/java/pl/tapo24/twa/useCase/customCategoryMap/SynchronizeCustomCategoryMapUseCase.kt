package pl.tapo24.twa.useCase.customCategoryMap

import android.content.Context
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.MapCategory
import pl.tapo24.twa.exceptions.HttpMessage
import pl.tapo24.twa.module.CustomCategoryModule
import pl.tapo24.twa.useCase.customCategory.SynchronizeCustomCategoryUseCase
import javax.inject.Inject

class SynchronizeCustomCategoryMapUseCase @Inject constructor(
    private val context: Context,
    private val tapoDb: TapoDb
) {
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface CustomCategoryProviderEntryPoint {
        fun customCategoryModule(): CustomCategoryModule
    }


    fun synchronize() {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(context, CustomCategoryProviderEntryPoint::class.java)
        val customCategoryModule = hiltEntryPoint.customCategoryModule()
        MainScope().launch(Dispatchers.IO) {
            val listToDeleteMapCategory = async {  tapoDb.mapCategory().getAllToDelete() }.await()
            if (listToDeleteMapCategory != null && listToDeleteMapCategory!!.isNotEmpty()) {
                listToDeleteMapCategory!!.forEach { mapCategory ->
                    var result: Result<String>? = null
                    async { result = customCategoryModule.deleteMapCategory(mapCategory) }.await()
                    result?.onSuccess {
                        async { tapoDb.mapCategory().delete(mapCategory) }.await()
                    }
                    result?.onFailure {
                        if (it.message == HttpMessage.ThisCustomCategoryMapNotExist.message) {
                            tapoDb.mapCategory().delete(mapCategory)
                        }
                    }
                }
                var mapCategoryFromDbAfterDelete: List<MapCategory>? =
                async { tapoDb.mapCategory().getAllNotSynchronized() }.await()
                if (mapCategoryFromDbAfterDelete != null && mapCategoryFromDbAfterDelete!!.isNotEmpty()) {
                    mapCategoryFromDbAfterDelete!!.forEach { mapCategory ->
                        var result: Result<String>? = null
                        async { result = customCategoryModule.putMapCategory(mapCategory) }.await()
                        result?.onSuccess {
                            mapCategory.dataSynchronized = true
                            async { tapoDb.mapCategory().insert(mapCategory) }.await()
                        }
                    }
                }
            }
        }
    }
}