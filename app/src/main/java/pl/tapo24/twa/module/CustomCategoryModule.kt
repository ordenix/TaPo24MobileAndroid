package pl.tapo24.twa.module

import android.content.Context
import android.text.BoringLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pl.tapo24.twa.data.NetworkTypes
import pl.tapo24.twa.data.State
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.CustomCategory
import pl.tapo24.twa.db.entity.Setting
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
                val customCategoriesFromDb = async { tapoDb.customCategory().getAllWithoutDeleted() }.await()
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

    fun putCustomCategory(customCategoryList: List<CustomCategory>): Result<String> {
        var statusOk: Boolean = true
        customCategoryList.forEach { customCategory ->
            val response = networkClient.putCustomCategory(State.jwtToken, customCategory)
            response.onFailure {
                statusOk = false
            }

        }
        return if (statusOk) {
            Result.success("OK")
        } else {
            Result.failure(Exception("Error"))
        }

    }

    suspend fun getLastSortId(): Int {
        var lastSortId = 0
        MainScope().async(Dispatchers.IO) {
            async { lastSortId = tapoDb.customCategory().getCountOfElements() }.await()
        }.await()
        return lastSortId
    }

    fun deleteCustomCategory(customCategory: CustomCategory): Result<String> {
        val response = networkClient.deleteCustomCategory(State.jwtToken, customCategory)
        return response
    }

    suspend fun getShowOnTopParameter(): Boolean {
        var showOnTop = true
        MainScope().async(Dispatchers.IO) {
            val settingFromDb = async { tapoDb.settingDb().getSettingByName("showOnTop") }.await()
            if (settingFromDb != null) {
                showOnTop = settingFromDb.state
            } else {
                val setting = Setting(name = "showOnTop", state = true)
                async { tapoDb.settingDb().insert(setting) }.await()
            }

        }.await()
        return  showOnTop
    }

    fun changeShowOnTopParameter(showOnTop: Boolean) {
        MainScope().launch(Dispatchers.IO) {
            val setting = Setting(name = "showOnTop", state = showOnTop)
            async { tapoDb.settingDb().insert(setting) }.await()
        }
    }





}