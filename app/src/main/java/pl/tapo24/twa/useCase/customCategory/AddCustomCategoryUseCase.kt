package pl.tapo24.twa.useCase.customCategory

import android.content.Context
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import pl.tapo24.twa.R
import pl.tapo24.twa.data.NetworkTypes
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.CustomCategory
import pl.tapo24.twa.exceptions.InternalException
import pl.tapo24.twa.module.CustomCategoryModule
import pl.tapo24.twa.utils.CheckConnection
import javax.inject.Inject

class AddCustomCategoryUseCase @Inject constructor(private val tapoDb: TapoDb, private val context: Context) {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface CustomCategoryProviderEntryPoint {
        fun customCategoryModule(): CustomCategoryModule
    }


    suspend fun run(name: String, sortOrder: Int): Result<String> {
         if (CheckConnection().getConnectionType(context) != NetworkTypes.None) {
            val hiltEntryPoint = EntryPointAccessors.fromApplication(context, CustomCategoryProviderEntryPoint::class.java)
            val customCategoryModule = hiltEntryPoint.customCategoryModule()
            var response: Result<CustomCategory>? = null
            MainScope().async(Dispatchers.IO) {
                response = customCategoryModule.putCustomCategory(name, sortOrder)
            }.await()
            response!!.onSuccess {
                MainScope().async(Dispatchers.IO) {
                    async { tapoDb.customCategory().insert(it) }.await()
                }.await()
                return Result.success(context.getString(R.string.succesAddCustomCategory))
            }
             response!!.onFailure {

                 return Result.failure(InternalException(it.message))
             }
             return Result.failure(InternalException("Unknown error"))

        } else {
             return Result.failure(Exception(context.getString(R.string.networkRequiedToAddCustomCategory)))
        }


    }


}