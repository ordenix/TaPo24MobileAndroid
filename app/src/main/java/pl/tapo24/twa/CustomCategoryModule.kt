package pl.tapo24.twa

import kotlinx.coroutines.*
import pl.tapo24.twa.data.State
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.CustomCategory
import pl.tapo24.twa.db.entity.MapCategory
import pl.tapo24.twa.db.entity.Setting
import pl.tapo24.twa.infrastructure.NetworkClient
import javax.inject.Inject

class CustomCategoryModule  @Inject constructor(private var tapoDb: TapoDb, private var networkClient: NetworkClient) {

    fun synchronizeOnSessionCreated() {
        MainScope().launch(Dispatchers.IO) {
            if (State.internetStatus.value != 0 && State.isLogin.value == true && !State.isCustomCategorySynchronized) {
                var offline: Setting? = null
                async { offline  =  tapoDb.settingDb().getSettingByName("offlineToSendCustomCategory")}.await()
                if (offline == null) {
                    // empty offline
                    State.isCustomCategorySynchronized = true
                    TODO()
                } else {
                    // send offline stack
                    sendToServerCategoryMap()
                    sendToServerCustomCategory()
                }
            }
        }
    }

    private suspend fun receiveFromServerCustomCategory(): List<CustomCategory> {
        var listCustomCategory: List<CustomCategory> = listOf()
        if (State.internetStatus.value != 0) {
            MainScope().async {
                withContext(Dispatchers.IO) {
                    val response = networkClient.getCustomCategoryList(State.jwtToken)
                    response.onSuccess {
                        listCustomCategory = it
                    }
                }

            }.await()
        }
        return listCustomCategory
    }
    private suspend fun receiveFromServerCustomMap(): List<MapCategory> {
        var listCustomCategory: List<MapCategory> = listOf()
        if (State.internetStatus.value != 0) {
            MainScope().async {
                withContext(Dispatchers.IO) {
                    val response = networkClient.getCustomMapList(State.jwtToken)
                    response.onSuccess {
                        listCustomCategory = it
                    }
                }

            }.await()
        }
        return listCustomCategory
    }
    private suspend fun sendToServerCustomCategory() {
        if (State.internetStatus.value != 0) {
            MainScope().async {
                withContext(Dispatchers.IO) {
                TODO()
                }
            }.await()
        }
    }
    private suspend fun sendToServerCategoryMap() {
        if (State.internetStatus.value != 0) {
            MainScope().async {
                withContext(Dispatchers.IO) {
                    TODO()
                }
            }.await()
        }
    }

    fun preformSendToServer() {
        TODO()
    }
}