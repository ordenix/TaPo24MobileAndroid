package pl.tapo24.twa.updater

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.fragment.app.FragmentManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.*
import pl.tapo24.twa.data.State
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.AssetList
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.dbData.entity.Law
import pl.tapo24.twa.infrastructure.NetworkClient
import java.io.File
import java.lang.IllegalStateException
import kotlin.system.exitProcess

class AssetUpdater(
    val tapoDb: TapoDb,
    val dataTapoDb: DataTapoDb,
    val networkClient: NetworkClient,
    val context: Context,
    val childFragmentManager: FragmentManager
) {

    private  val dialog = DialogDataUpdater()

    fun getAllData() {
        MainScope().launch(Dispatchers.IO) {
            var existAssetList = false
            async { existAssetList = tapoDb.assetListDb().exist() }.await()
            if (State.internetStatus != 0) {
                // Network available
                if (State.networkType == "All") {
                    // download because all
                    getAssets()

                } else if (State.networkType == "WiFi" && State.internetStatus == 2) {
                    // download because condition valid uesr have network WiFi
                    getAssets()
                } else if (!existAssetList) {
                    withContext(Dispatchers.Main) {
                        dialogCloseApp()
                    }
                }
            } else if (!existAssetList) {
                withContext(Dispatchers.Main) {
                    dialogCloseApp()
                }
            }

        }
    }

    private fun dialogCloseApp() {

        val dialogClose = MaterialAlertDialogBuilder(context)
            .setTitle("UWAGA BRAK DANYCH")
            .setMessage("Nie pobrano wcześniej wymaganych danych, w związku z tym korzystanie z aplikacji nie jest możliwe")
            .setCancelable(false)
            .setPositiveButton("Zamknij") { dialog, which ->
                exitProcess(0)

            }
            .show()


    }



    private suspend fun downloadAsset(type: String, name: String) {
        MainScope().async {
            // TODO: CHANGE IT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            val request = DownloadManager.Request(Uri.parse("https://develop.api3.tapo24.pl/api/data/resources/?type=$type&name=$name"))
            request.setDescription("Plik $name jest w trakcie pobierania")
            request.allowScanningByMediaScanner()
            request.setTitle("Pobieranie pliku $name")
            // request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            //Set the local destination for the downloaded file to a path within the application's external files directory
            //Set the local destination for the downloaded file to a path within the application's external files directory
            //To Store file in External Public Directory use "setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)"
            //request.set
//                    request.setDestinationInExternalPublicDir(
//                        Environment.DIRECTORY_DOWNLOADS,
//                        "tapo24/pdf/${element.type}/${element.fileName}"
//                    )
            request.setDestinationInExternalFilesDir(
                context,
                Environment.DIRECTORY_DOWNLOADS,
                "$type/$name"
            )



            val downloadManager = context.getSystemService(DownloadManager::class.java)!!
            // context.getSystemService(Context.DOWNLOAD_SERVICE)  as DownloadManager?
            val downloadId: Long = downloadManager.enqueue(request)
            val q = DownloadManager.Query().setFilterById(downloadId)

            var downloadFinished = false
            while (!downloadFinished) {
                val cursor = downloadManager.query(q)
                if (cursor.moveToFirst()) {
                    val colStatus = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)

                    if (colStatus > 0) {
                        when (cursor.getInt(colStatus)) {
                            DownloadManager.STATUS_FAILED -> {downloadFinished = true }
                            DownloadManager.STATUS_PAUSED -> { }
                            DownloadManager.STATUS_PENDING -> { }
                            DownloadManager.STATUS_RUNNING -> {

                                val totalBL =cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                                if (totalBL> 0) {
                                    val totalBytes = cursor.getLong(totalBL)
                                    val downloadBytesL = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                                    if (downloadBytesL > 0) {
                                        val downloadBytes = cursor.getLong(downloadBytesL)
                                        var progres: Long = 0
                                        if (totalBytes > 0) {
                                            progres = (downloadBytes * 100 / totalBytes)
                                        }
                                        withContext(Dispatchers.Main ){
                                            if (dialog.isVisible) {
                                                dialog.setProgres(progres.toInt())

                                            }
                                        }

                                        println(progres)
                                    }
                                }

                            }
                            DownloadManager.STATUS_SUCCESSFUL -> {
                                withContext(Dispatchers.Main ){
                                    if (dialog.isVisible) {
                                        dialog.setProgres(100)

                                    }
                                }

                                downloadFinished = true
                            }
                        }



                    }

                }  else {
                    downloadFinished = true

                }
                delay(10L)
                cursor.close()
            }
        }.await()
    }

    private fun getAssets() {
        MainScope().launch(Dispatchers.IO) {
            var listAsset: List<AssetList>? = null
            var listAssetFromServer: List<AssetList>? =null
            var listLawFromServer: List<Law>? =null
            var listLaw: List<Law>? =null
            async {
                val response = networkClient.getAssetListData()
                    response.onSuccess {
                        listAssetFromServer = it
                    }
                  }.await()
            async { listAsset = tapoDb.assetListDb().getAll() }.await()
            async {
                val response = networkClient.getLawData()
                response.onSuccess {
                    listLawFromServer = it
                }
            }.await()
            async { listLaw = dataTapoDb.law().getAll() }.await()

            if (!listLaw.isNullOrEmpty()) {
                // update law
                listLawFromServer?.forEach {element ->
                    val elementFromDb = listLaw?.find { el -> el.id == element.id }
                    if (element.version!! > (elementFromDb?.version ?: 0)) {
                        // download updatet or anew dd
                        if (!dialog.isVisible) {
                            dialog.show(childFragmentManager, "Data")
                        }
                        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "pdf/${element.fileName}")
                        // val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "tapo24/pdf/${element.type}/${element.fileName}")
                        file.delete()

                        withContext(Dispatchers.Main) {
                            delay(10)
                            if (dialog.isVisible) {
                                dialog.setBody("Pobieranie: ${element.name}")

                            }
                        }
                        async { element.fileName?.let { downloadAsset("pdf" , it) } }.await()

                        async { dataTapoDb.law().insert(element) }.await()

                    }
                    // dialog.dismiss()
                }
                listLaw?.forEach { element ->
                    if(listLawFromServer?.find { el -> el.id == element.id } == null) {
                        if (!dialog.isVisible) {
                            dialog.show(childFragmentManager, "Data")
                        }
                        withContext(Dispatchers.Main) {
                            delay(10)
                            if (dialog.isVisible) {
                                dialog.setBody("Kasowanie starych plików")
                                dialog.setIndeterminate()
                            }

                        }
                        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "pdf/${element.fileName}")
                        file.delete()
                        async { dataTapoDb.law().deleteElement(element) }.await()
                    }
                }
                if (dialog.isVisible) {
                    dialog.dismiss()

                }

            }

            if (!listAsset.isNullOrEmpty()) {
                // update asset

                listAssetFromServer?.forEach {element ->
                    val elementFromDb = listAsset?.find { el -> el.id == element.id }
                    if (element.version > (elementFromDb?.version ?: 0)) {
                        // download updatet or anew dd
                        if (!dialog.isVisible) {
                            dialog.show(childFragmentManager, "Data")
                        }
                        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "${element.path}/${element.name}")
                        // val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "tapo24/pdf/${element.type}/${element.fileName}")
                        file.delete()

                        withContext(Dispatchers.Main) {
                            delay(10)
                            if (dialog.isVisible) {
                                dialog.setBody("Pobieranie: ${element.name}")

                            }
                        }
                        async { downloadAsset(element.path , element.name) }.await()
                        async { tapoDb.assetListDb().insert(element) }.await()

                    }
                    // dialog.dismiss()
                }
                listAsset?.forEach { element ->
                    if(listAssetFromServer?.find { el -> el.id == element.id } == null) {
                        if (!dialog.isVisible) {
                            dialog.show(childFragmentManager, "Data")
                        }
                        withContext(Dispatchers.Main) {
                            delay(10)
                            if (dialog.isVisible) {
                                dialog.setBody("Kasowanie starych plików")
                                dialog.setIndeterminate()
                            }

                        }
                        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "${element.path}/${element.name}")
                        file.delete()
                        async { tapoDb.assetListDb().deleteElement(element) }.await()
                    }
                }
                if (dialog.isVisible) {
                    dialog.dismiss()

                }

            } else {
                // downoload all because init
                withContext(Dispatchers.Main) {
                    if (!dialog.isVisible) {
                        dialog.show(childFragmentManager, "Data")
                        delay(10)
                        dialog.setBody("Pobieranie głównej paczki")
                    }

                }

                downloadAsset("package" ,"package_main.zip")
                delay(100)
                val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "package/package_main.zip")
                withContext(Dispatchers.Main) {
                    if (dialog.isVisible) {
                        dialog.setBody("Dekompresja głównej paczki w tym czasie możesz iśc na kawę ;)")
                        dialog.setIndeterminate()
                    }

                }
                context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.toURI()?.path?.let { PackageExtractor.unzip(it, file) }

                file.delete()
                withContext(Dispatchers.Main) {
                    if (dialog.isVisible) {
                        dialog.setDone()

                    }
                    delay(1000)
                    if (dialog.isVisible) {
                        try {
                            dialog.dismiss()
                        } catch (_: IllegalStateException) {

                        }


                    }
                }
                listAssetFromServer?.let { tapoDb.assetListDb().insertList(it) }
                listLawFromServer?.let { dataTapoDb.law().insertList(it) }
            }
            val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "package")
            if (directory.isDirectory){
                for (listFile in directory.listFiles()) {
                    listFile.delete()
                }
            }
            directory.delete()
        }

// val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "${element.type}/${element.fileName}")
        // val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "tapo24/pdf/${element.type}/${element.fileName}")

        // file.delete()

        //Set the local destination for the downloaded file to a path within the application's external files directory
        //Set the local destination for the downloaded file to a path within the application's external files directory
        //request.set
//                    request.setDestinationInExternalPublicDir(
//                        Environment.DIRECTORY_DOWNLOADS,
//                        "tapo24/pdf/${element.type}/${element.fileName}"
//                    )
        // progres and notify https://stackoverflow.com/questions/65164785/using-progressbar-with-downloadmanager
//To Store file in External Public Directory use "setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)"

    }


}
