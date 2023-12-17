package pl.tapo24.twa.updater

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Mourning
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.exceptions.InternalException
import pl.tapo24.twa.infrastructure.NetworkClient
import javax.inject.Inject

class MourningCheck @Inject constructor(private var tapoDb: TapoDb, private var networkClient: NetworkClient) {

//    fun runCheck() {
//        if (Stat)
//            // brodcast service
//            // bound service
//    }

    private suspend fun deleteMourning() {
        MainScope().async(Dispatchers.IO) {
            async {
                tapoDb.mourning().nukeTable()
            }.await()
        }.await()
    }

//    private suspend fun getLastMourning(): Mourning? {
//        var mourning: Mourning? = null
//        MainScope().async {
//           mourning = tapoDb.mourning().getLast()
//        }.await()
//        return mourning
//    }
    suspend fun getMourningFromService(): Result<String> {
        var resultText: String = "Error"
        MainScope().async (Dispatchers.IO) {
            async {
                val response = networkClient.getMourningData()
                response.onSuccess {
                    deleteMourning()
                    tapoDb.mourning().insertList(it)
                    resultText = "Ok"
                }
            }.await()
        }.await()
        if (resultText == "Error") {
            return Result.failure(InternalException("Error"))
        } else {
            return Result.success("Success")
        }

    }
    suspend fun checkIsMourningActive(): Result<Boolean> {
        var status = false
        MainScope().async (Dispatchers.IO) {
            var mourning: Mourning? = null
            async {  mourning =  tapoDb.mourning().getLast() }.await()
            if (mourning != null) {
                val currenTime: Long = System.currentTimeMillis()/1000
                if (mourning!!.dataFrom  < currenTime && mourning!!.dataTo > currenTime) {
                    status = true
                }
            }


        }.await()

        return Result.success(status)
    }


}