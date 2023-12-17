package pl.tapo24.twa

import kotlinx.coroutines.*
import pl.tapo24.twa.data.NetworkTypes
import pl.tapo24.twa.data.State
import pl.tapo24.twa.data.profile.BodyOffenses
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Setting
import pl.tapo24.twa.db.entity.Tariff
import pl.tapo24.twa.infrastructure.NetworkClient
import javax.inject.Inject

class FavouriteModule @Inject constructor(private var tapoDb: TapoDb, private var networkClient: NetworkClient) {

    fun synchronizeOnSessionCreatedOrInternetAvailable() {
        /// check stack offline

        MainScope().launch(Dispatchers.IO) {
            if (State.internetStatus.value != NetworkTypes.None && State.isLogin.value == true && !State.isFavouritesSynchronized) {
                var offline: Setting? = null
                async { offline  =  tapoDb.settingDb().getSettingByName("offlineToSendFavourite")}.await()
                if (offline == null) {
                    // empty offline
                    State.isFavouritesSynchronized = true
                    var responseList: List<String>? = null
                    async { responseList = receivedFavListToServer() }.await()

                    if (responseList != null && responseList!!.isNotEmpty()) {
                        // set from server
                        var listFavToClear: List<Tariff>? = null
                        async { listFavToClear = tapoDb.tariffDb().getFavByEngine("New") }.await()
                        if (listFavToClear != null && listFavToClear!!.isNotEmpty()) {
                            listFavToClear!!.forEach { element->
                                element.sortOrderFav = 0
                                element.favorites = false
                                async { tapoDb.tariffDb().insert(element)  }.await()
                            }

                        }
                        responseList!!.forEachIndexed { index, element ->
                            var tariffData: Tariff? = null
                            async {
                                tariffData = tapoDb.tariffDb().getByDocId(element)
                            }.await()
                            if (tariffData != null) {
                                tariffData!!.sortOrderFav = index
                                tariffData!!.favorites = true
                                async { tapoDb.tariffDb().insert(tariffData!!) }.await()
                            }

                        }
                    } else {
                        async { sendFavListToServer() }.await()
                    }
                } else {
                    //send offline to server
                    sendFavListToServer()

                }
            }

        }
    }

    private suspend fun sendFavListToServer() {
        if (State.internetStatus.value != NetworkTypes.None) {
            MainScope().async {
                withContext(Dispatchers.IO){
                    var tariffFav: List<Tariff>? = null
                    async { tariffFav = tapoDb.tariffDb().getFavByEngine("New") }.await()
                    if (tariffFav != null && tariffFav!!.isNotEmpty()) {
                        val favList: MutableList<String> = mutableListOf()
                        tariffFav!!.forEach { element ->
                            favList.add(element.id)
                        }
                        val listToSend: MutableList<String> = mutableListOf()
                        favList.forEach { element ->
                            listToSend.add("\"$element\"")
                        }
                        var stringToServer: String  = ""
                        stringToServer = "[${listToSend.joinToString(",")}]"
                        val bodyToPut = BodyOffenses(stringToServer)
                        val response = networkClient.putFavoritesOffenses(State.jwtToken, bodyToPut)
                        response.onSuccess {
                            State.isFavouritesSynchronized  = true
                            try {
                                var settingToDel: Setting? = null
                                async { settingToDel = tapoDb.settingDb().getSettingByName("offlineToSendFavourite") }.await()
                                if (settingToDel != null) {
                                    async { tapoDb.settingDb().deleteElement(settingToDel!!) }.await()
                                }

                            } catch (ex: Throwable) {
                                println(ex)
                            }
                        }
                    }


                }

            }.await()
        }


    }

    private suspend fun receivedFavListToServer(): List<String> {
        var responseBody: BodyOffenses? = null
        MainScope().async {
                withContext(Dispatchers.IO) {
                    val response = networkClient.getFavoritesOffenses(State.jwtToken)

                    response.onSuccess {
                        responseBody = it
                    }
                }

        }.await()
        var favArrayFromServer: List<String>? = null
        val favArray: MutableList<String> = mutableListOf()
        if (responseBody != null) {
            responseBody!!.offensesList = responseBody!!.offensesList.replace("[","")
            responseBody!!.offensesList = responseBody!!.offensesList.replace("]","")
            favArrayFromServer = responseBody!!.offensesList.split(",")
            favArrayFromServer.forEach {element ->
                if (element.length > 1 && !element.contains("null")) {
                    favArray.add(element.replace("\"",""))
                }

            }
        }
        return favArray
    }
    fun performSendFavList() {
        // if offline then State.isFavouritesSynchronized = false
        if (State.isLogin.value == true) {
            MainScope().launch(Dispatchers.IO) {
                if (State.internetStatus.value != NetworkTypes.None) {
                    // interet avaliable
                    sendFavListToServer()
                } else {
                    State.isFavouritesSynchronized = false
                    val settingToOffline: Setting = Setting("offlineToSendFavourite","To send")
                    async { tapoDb.settingDb().insert(settingToOffline) }.await()
                    // offline
                }
            }
        }


    }

}