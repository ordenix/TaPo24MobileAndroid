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
import pl.tapo24.twa.db.entity.MapCategory
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

    //map list

    suspend fun getCustomCategoryMapList(): Result<List<MapCategory>> {
        val mapCategoryList: MutableList<MapCategory> = mutableListOf()
        MainScope().async(Dispatchers.IO) {
            if (CheckConnection().getConnectionType(context) != NetworkTypes.None) {
                val response = networkClient.getCustomCategoryMapList(State.jwtToken)
                response.onSuccess { responseMapCategory ->
                    var listFromDb: List<MapCategory>? = null
                    async { listFromDb = tapoDb.mapCategory().getAll() }.await()
                    mapCategoryList.addAll(responseMapCategory)
                    mapCategoryList.forEach { mapCategory ->
                        val elementFind = listFromDb!!.find {
                                it.customCategoryId == mapCategory.customCategoryId
                                && it.tariffId == mapCategory.tariffId }
                        if (elementFind != null) {
                            mapCategory.apply {
                                dataSynchronized = elementFind.dataSynchronized
                                toDelete = elementFind.toDelete
                            }
                        }
                    }
                    async { tapoDb.mapCategory().nukeTable() }.await()
                    async { tapoDb.mapCategory().insertAll(mapCategoryList) }.await()
                }

            } else {
                val mapCategoryListFromDb = async { tapoDb.mapCategory().getAllWithoutDeleted() }.await()
                mapCategoryList.addAll(mapCategoryListFromDb)
            }
        }.await()
        return Result.success(mapCategoryList)
    }

    fun putMapCategory(mapCategory: MapCategory): Result<String> {
        val response = networkClient.putCustomCategoryMap(State.jwtToken, mapCategory)
        return response
    }

    fun deleteMapCategory(mapCategory: MapCategory): Result<String> {
        val response = networkClient.deleteCustomCategoryMap(State.jwtToken, mapCategory)
        return response
    }





}