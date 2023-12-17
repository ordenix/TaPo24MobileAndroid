package pl.tapo24.twa.updater

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.infrastructure.NetworkClient

class MourningCheck(
    val tapoDb: TapoDb,
    val networkClient: NetworkClient,
) {

//    fun runCheck() {
//        if (Stat)
//            // brodcast service
//            // bound service
//    }

    private fun deleteMourning() {
        MainScope().launch(Dispatchers.IO) {
            async {
                tapoDb.mourning().nukeTable()
            }.await()
        }
    }
    private fun getMourning() {
        MainScope().launch(Dispatchers.IO) {
            async {
                val response = networkClient.getMourningData()
                response.onSuccess {
                    tapoDb.mourning().insertList(it)
                }
            }.await()
        }
    }

}