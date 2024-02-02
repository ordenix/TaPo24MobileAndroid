package pl.tapo24.twa.updater

import android.content.Context
import kotlinx.coroutines.*
import pl.tapo24.twa.data.State
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
        MainScope().launch(Dispatchers.IO) {
            val isPublicStorage = async { getStorageIsPublic() }.await()
            networkClient.getAssetListData().onSuccess { assetDataFromServer ->
                if (assetDataFromServer.isNotEmpty()) {
                    val assetDataFromDb = async { tapoDb.assetListDb().getAll() }.await()
                    assetDataFromServer.forEach { asset ->
                     // update or add new
                        val assetFromDb = assetDataFromDb.find { it.id == asset.id }
                        val versionFromServer = asset.version ?: 0
                        val versionFromDb: Int = assetFromDb?.version ?: 0
                        if ((versionFromServer > versionFromDb || force)) {
                            // download file
                            State.dialogDownloadFileMessage = "Pobieranie pliku ${asset.name}"
                            async {
                                Downloader().deleteAsset(
                                    asset.path,
                                    asset.name,
                                    context,
                                    isPublicStorage
                                )
                            }.await()
                            val resultDownloadFile = Downloader().downloadAsset(
                                asset.path,
                                asset.name,
                                context,
                                isPublicStorage
                            )
                            resultDownloadFile.onSuccess {
                                async { tapoDb.assetListDb().insert(asset) }.await()
                            }
                        }
                    }
                    //
                    State.dialogDownloadFileMessage = "Kasowanie starych plików"
                    withContext(Dispatchers.Main) {
                        State.setIndeterminate.value = true
                    }
                    assetDataFromDb.forEach { assetDataFromDb ->
                        if (assetDataFromServer.find { el-> el.id == assetDataFromDb.id } == null) {
                            // delete file
                            async {
                                Downloader().deleteAsset(
                                    assetDataFromDb.path,
                                    assetDataFromDb.name,
                                    context,
                                    isPublicStorage
                                )
                            }.await()
                            async { tapoDb.assetListDb().deleteElement(assetDataFromDb) }.await()
                        }
                    }
                }
            }
        }
    }


    fun initUpdate() {
        MainScope().launch(Dispatchers.IO) {
            networkClient.getAssetListData().onSuccess { assetData ->
                tapoDb.assetListDb().insertList(assetData)
            }
        }
    }

    private suspend fun getStorageIsPublic(): Boolean {
        var isPublicStorage = false
        MainScope().async(Dispatchers.IO) {
            isPublicStorage = tapoDb.settingDb().getSettingByName("publicStorage")?.state ?: false
        }.await()
        return isPublicStorage
    }



}