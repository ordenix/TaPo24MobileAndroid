package pl.tapo24.twa.updater

import android.content.Context
import kotlinx.coroutines.*
import pl.tapo24.twa.data.State
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.dbData.entity.Law
import pl.tapo24.twa.infrastructure.NetworkClient
import javax.inject.Inject

class LawUpdater @Inject constructor(
    private val context: Context,
    private val networkClient: NetworkClient,
    private val tapoDb: TapoDb,
    private val dataTapoDb: DataTapoDb,
) {

    fun update(force: Boolean = false, jwt: String?) {
        MainScope().launch(Dispatchers.IO) {
            val isPublicStorage = async { getStorageIsPublic() }.await()
            networkClient.getLawData(jwt).onSuccess { lawDataFromServer ->
                if (lawDataFromServer.isNotEmpty()) {
                    val lawDataFromDb =  async { dataTapoDb.law().getAll() }.await()
                    lawDataFromServer.forEach { law ->
                        // update or add new file
                        val lawFromDb = lawDataFromDb.find { it.id == law.id }
                        val versionFromServer = law.version ?: 0
                        val versionFromDb: Int = lawFromDb?.version ?: 0
                        val isSelectToDownload =  lawFromDb?.isSelectedToDownload ?: false
                        if ((versionFromServer > versionFromDb || force)
                            && law.fileName != null
                            && (law.isOptional != true || isSelectToDownload) ) {
                            // download file
                            State.dialogDownloadFileMessage = "Pobieranie pliku ${law.fileName}"
                            async {
                                Downloader().deleteAsset(
                                    "pdf",
                                    law.fileName!!,
                                    context,
                                    isPublicStorage
                                )
                            }.await()
                            val resultDownloadFile = Downloader().downloadAsset(
                                "pdf",
                                law.fileName!!,
                                context,
                                isPublicStorage
                            )
                            resultDownloadFile.onSuccess {
                                if (law.isOptional != true) {
                                    law.isSelectedToDownload = true
                                } else {
                                    law.isSelectedToDownload = lawFromDb?.isSelectedToDownload ?: false
                                }

                                async { dataTapoDb.law().insert(law) }.await()
                            }

                        } else if ((versionFromServer > versionFromDb || force)
                            && law.fileName != null
                            && (law.isOptional == true && !isSelectToDownload) ) {
                            law.isSelectedToDownload = false
                            async { dataTapoDb.law().insert(law) }.await()

                        }

                    }
                    State.dialogDownloadFileMessage = "Kasowanie starych plików"
                    withContext(Dispatchers.Main) {
                        State.setIndeterminate.value = true
                    }

                    lawDataFromDb.forEach { lawDataFromDb ->
                        if (lawDataFromServer.find { el-> el.id == lawDataFromDb.id } == null) {
                            // delete file
                            async {
                                Downloader().deleteAsset(
                                    "pdf",
                                    lawDataFromDb.fileName!!,
                                    context,
                                    isPublicStorage
                                )
                            }.await()
                            async { dataTapoDb.law().deleteElement(lawDataFromDb) }.await()
                        }

                    }
                    withContext(Dispatchers.Main) {
                        State.dialogDownloadFileProgress.value = null
                    }

                }
            }
        }

    }

    suspend fun downloadOptionalFile(law: Law) {
        MainScope().async(Dispatchers.IO) {
            val isPublicStorage = async { getStorageIsPublic() }.await()
            State.dialogDownloadFileMessage = "Pobieranie pliku ${law.fileName}"
            async {
                Downloader().deleteAsset(
                    "pdf",
                    law.fileName!!,
                    context,
                    isPublicStorage
                )
            }.await()
            val resultDownloadFile = Downloader().downloadAsset(
                "pdf",
                law.fileName!!,
                context,
                isPublicStorage
            )
            resultDownloadFile.onSuccess {
                async { dataTapoDb.law().insert(law) }.await()
                withContext(Dispatchers.Main) {
                    State.dialogDownloadFileProgress.value = null
                }
            }
        }.await()
    }

    suspend fun deleteOptionalFile(law: Law) {
        MainScope().async(Dispatchers.IO) {
            val isPublicStorage = async { getStorageIsPublic() }.await()
            State.dialogDownloadFileMessage = "Kasowanie starych plików"
            async {
                Downloader().deleteAsset(
                    "pdf",
                    law.fileName!!,
                    context,
                    isPublicStorage
                )
            }.await()

            async { dataTapoDb.law().insert(law) }.await()
            withContext(Dispatchers.Main) {
                State.dialogDownloadFileProgress.value = null
            }

        }.await()
    }

    fun initUpdate() {
        MainScope().launch(Dispatchers.IO) {
            networkClient.getLawData(jwt = null).onSuccess { lawData ->
                dataTapoDb.law().insertList(lawData)
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