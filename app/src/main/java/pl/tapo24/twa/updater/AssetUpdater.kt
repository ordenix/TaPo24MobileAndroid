package pl.tapo24.twa.updater

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.infrastructure.NetworkClient
import javax.inject.Inject

class AssetUpdater @Inject constructor(
    private val context: Context,
    private val networkClient: NetworkClient,
    private val tapoDb: TapoDb,
    private val dataTapoDb: DataTapoDb,
) {




    fun update(force: Boolean = false) {
        // TODO
    }


    fun initUpdate() {

    }

    private suspend fun getStorageIsPublic(): Boolean {
        var isPublicStorage = false
        MainScope().async(Dispatchers.IO) {
            isPublicStorage = tapoDb.settingDb().getSettingByName("publicStorage")?.state ?: false
        }.await()
        return isPublicStorage
    }



}