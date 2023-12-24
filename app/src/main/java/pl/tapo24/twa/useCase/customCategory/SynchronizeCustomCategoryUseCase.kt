package pl.tapo24.twa.useCase.customCategory

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
import pl.tapo24.twa.db.entity.CustomCategory
import pl.tapo24.twa.infrastructure.NetworkClient
import pl.tapo24.twa.module.CustomCategoryModule
import javax.inject.Inject

class SynchronizeCustomCategoryUseCase @Inject constructor(
    private val context: Context,
    private val tapoDb: TapoDb,
    private val networkClient: NetworkClient
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
            var customCategoryToDelete: List<CustomCategory>? = null
            async { customCategoryToDelete = tapoDb.customCategory().getAllToDelete() }.await()
            if (customCategoryToDelete != null && customCategoryToDelete!!.isNotEmpty()) {
                customCategoryToDelete!!.forEach { customCategory ->
                    var result: Result<String>? = null
                    async { result = customCategoryModule.deleteCustomCategory(customCategory) }.await()
                    result?.onSuccess {
                        async { tapoDb.customCategory().delete(customCategory) }.await()
                    }
                }
                var customCategoryFromDbAfterDelete: List<CustomCategory>? = null
                async { customCategoryFromDbAfterDelete = tapoDb.customCategory().getAll() }.await()
                if (customCategoryFromDbAfterDelete != null && customCategoryFromDbAfterDelete!!.isNotEmpty()) {
                    customCategoryFromDbAfterDelete!!.forEachIndexed { index, customCategory ->
                        customCategory.apply {
                            sortOrder = index
                            dataSynchronized = false
                        }
                        async { tapoDb.customCategory().insert(customCategory) }.await()
                    }
                }
            }
            synchronizeCustomCategory(customCategoryModule)



        }
    }

    private  fun synchronizeCustomCategory(customCategoryModule: CustomCategoryModule){
        MainScope().launch(Dispatchers.IO) {
            var customCategoryFromDb: List<CustomCategory>? = null
            async { customCategoryFromDb = tapoDb.customCategory().getAllNotSynchronized() }.await()
            if (customCategoryFromDb != null && customCategoryFromDb!!.isNotEmpty()) {
                customCategoryFromDb!!.forEach { customCategory ->
                    var result: Result<CustomCategory>? = null
                    async { result = customCategoryModule.putCustomCategory(customCategory) }.await()
                    result?.onSuccess {
                        async { tapoDb.customCategory().insert(it) }.await()
                    }
                }
            }

        }

    }





}