package pl.tapo24.twa.module

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import pl.tapo24.twa.data.NetworkTypes
import pl.tapo24.twa.data.State
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.CustomCategory
import pl.tapo24.twa.infrastructure.NetworkClient
import pl.tapo24.twa.utils.CheckConnection
import javax.inject.Inject

class CustomCategoryModule @Inject constructor(private val context: Context, private val tapoDb: TapoDb, private val networkClient: NetworkClient) {

    suspend fun getCustomCategories(): Result<List<CustomCategory>> {
        val customCategories: MutableList<CustomCategory> = mutableListOf()
        MainScope().async(Dispatchers.IO) {
            if (CheckConnection().getConnectionType(context) != NetworkTypes.None) {
                val response = networkClient.getCustomCategoryList(State.jwtToken)
                response.onSuccess { elementResponse ->
                    var listFromDb: List<CustomCategory>? = null
                    async { listFromDb = tapoDb.customCategory().getAll() }.await()
                    customCategories.addAll(elementResponse)


                    customCategories.forEach { customCategory ->
                        val elementFind = listFromDb!!.find { it.id == customCategory.id }
                        if (elementFind != null) {
                            customCategory.apply {
                                dataSynchronized = elementFind.dataSynchronized
                                toDelete = elementFind.toDelete
                            }

                        }
                    }
                    async { tapoDb.customCategory().nukeTable() }.await()

                    async { tapoDb.customCategory().insertAll(customCategories) }.await()
                }
            } else {
                val customCategoriesFromDb = async { tapoDb.customCategory().getAll() }.await()
                customCategories.addAll(customCategoriesFromDb)
            }

        }.await()
        return Result.success(customCategories)
    }

    fun putCustomCategory(name: String, sortOrder: Int): Result<CustomCategory> {
        val customCategory = CustomCategory(categoryName = name, sortOrder = sortOrder)
        val response = networkClient.putCustomCategory(State.jwtToken, customCategory)
        return response

    }
    fun putCustomCategory(customCategory: CustomCategory): Result<CustomCategory> {
        val response = networkClient.putCustomCategory(State.jwtToken, customCategory)
        return response

    }

    fun deleteCustomCategory(customCategory: CustomCategory): Result<String> {
        val response = networkClient.deleteCustomCategory(State.jwtToken, customCategory)
        return response
    }



    //    fun deleteCustomCategory(customCategory: CustomCategory): Result<String> {
//      MainScope().launch(Dispatchers.IO) {
//        val response = networkClient.deleteCustomCategory(State.jwtToken, customCategory.id)
//
//      }

//        response.onSuccess {
//            MainScope().launch(Dispatchers.IO) {
//                async { tapoDb.customCategory().delete(customCategory) }.await()
//            }
//        }
//        return response
//}


}