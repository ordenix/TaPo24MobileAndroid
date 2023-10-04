package pl.tapo24.twa

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pl.tapo24.twa.data.State
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Setting
import pl.tapo24.twa.infrastructure.NetworkClient
import javax.inject.Inject

class FavouriteModule @Inject constructor(private var tapoDb: TapoDb, private var networkClient: NetworkClient) {

    fun synchronizeOnSessionCreatedOrInternetAvailable() {
        /// check stack offline
        MainScope().launch(Dispatchers.IO) {
            if (State.internetStatus.value != 0 && State.isLogin.value == true) {
                var offline: Setting? = null
                async { offline  =  tapoDb.settingDb().getSettingByName("offlineToSendFavourite")}.await()
                if (offline == null) {
                    // empty offline
                } else {
                    //send offline to server
                }
            }

        }
    }

    private suspend fun sendFavListToServer() {
        //add to stack check stack and execute
    }

    private suspend fun receivedFavListToServer() {
        var responseList
        MainScope().launch(Dispatchers.IO) {
            async {  }.await()
        }
    }

//    fun synchronizeDuringWorkingApp() {
//
//    }

    fun performSendFavList() {

    }

    private fun sendOffline() {

    }
}