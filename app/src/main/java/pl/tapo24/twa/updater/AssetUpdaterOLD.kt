package pl.tapo24.twa.updater

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.util.Log
import androidx.fragment.app.FragmentManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.*
import org.acra.ACRA
import pl.tapo24.twa.data.NetworkTypes
import pl.tapo24.twa.data.State
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.AssetList
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.dbData.entity.Law
import pl.tapo24.twa.exceptions.InternalException
import pl.tapo24.twa.infrastructure.NetworkClient
import java.io.File
import kotlin.system.exitProcess


class AssetUpdaterOLD(
    val tapoDb: TapoDb,
    val dataTapoDb: DataTapoDb,
    val networkClient: NetworkClient,
    val context: Context,
    val childFragmentManager: FragmentManager?,
    val activity: Activity
) {

    private  val dialog = DialogDataUpdater()

    fun getAllData() {
//        MainScope().launch(Dispatchers.IO) {
//            var existAssetList = false
//            async { existAssetList = tapoDb.assetListDb().exist() }.await()
//            if (State.internetStatus.value != NetworkTypes.None) {
//                // Network available
//                if (State.networkType == "All") {
//                    // download because all
//                    getAssets()
//
//                } else if (State.networkType == "WiFi" && State.internetStatus.value == NetworkTypes.WiFi) {
//                    // download because condition valid uesr have network WiFi
//                    getAssets()
//                } else if (!existAssetList) {
//                    withContext(Dispatchers.Main) {
//                        dialogCloseApp(true)
//                    }
//                }
//            } else if (!existAssetList) {
//                withContext(Dispatchers.Main) {
//                    dialogCloseApp()
//                }
//            }
//
//        }
    }
    private fun dialogErrorDuringMainPackage() {
        if (!activity.isFinishing){
            val dialogClose = MaterialAlertDialogBuilder(context)
                .setTitle("UWAGA, BŁĄD PODCZAS POBIERANIA GŁÓWNEJ PACZKI")
                .setMessage("Niestety wystąpił błąd podczas pobierania głównej paczki. Spróbuj uruchomić aplikację później. Do pobrania wymagane jest stabilne połączenie sieciowe. Jeżeli błąd się będzie się powtarzać skontaktuj się z nami pod adresem kontakt@tapo24.pl")
                .setCancelable(false)
                .setPositiveButton("Zamknij") { dialog, which ->
                    exitProcess(0)

                }
                .show()
        }

    }

    private fun dialogCloseApp(networkAvailable: Boolean = false) {
        if (networkAvailable) {
            val dialogClose = MaterialAlertDialogBuilder(context)
                .setTitle("UWAGA BRAK DANYCH")
                .setMessage("Nie pobrano wcześniej wymaganych danych. Przy inicjalizacji wybrano pobieranie danych tylko za pomocą WiFi w związku z tym korzystanie z aplikacji nie jest możliwe. Wykryto jednak dostęp do sieci komórkowych, pobranie danych za pomocą tej metody może wiązać się z dodatkowymi kosztami naliczonymi przez operatora.")
                .setCancelable(false)
                .setPositiveButton("Zamknij") { dialog, which ->
                    exitProcess(0)

                }
                .setNegativeButton("Pobierz mimo to") {
                    dialog, which ->
                    getAssets()
                    dialog.dismiss()
                }
                .show()
        } else {
            val dialogClose = MaterialAlertDialogBuilder(context)
                .setTitle("UWAGA BRAK DANYCH")
                .setMessage("Nie pobrano wcześniej wymaganych danych, w związku z tym korzystanie z aplikacji nie jest możliwe")
                .setCancelable(false)
                .setPositiveButton("Zamknij") { dialog, which ->
                    exitProcess(0)

                }
                .show()
        }




    }



    private suspend fun downloadAsset(type: String, name: String): Result<String> {
        var downloadFinished: Boolean = false
        var errorMessage: String =""
        var isError: Boolean = false
        MainScope().async {
            // TODO: CHANGE IT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            val request = DownloadManager.Request(Uri.parse("https://develop.api3.tapo24.pl/api/data/resources/?type=$type&name=$name"))
            request.setDescription("Plik $name jest w trakcie pobierania")
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
            while (!downloadFinished) {
                val cursor = downloadManager.query(q)
                if (cursor.moveToFirst()) {
                    val colStatus = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    if (colStatus > 0) {
                        when (cursor.getInt(colStatus)) {
                            DownloadManager.STATUS_FAILED -> {
                                downloadFinished = true
                                isError = true
                                errorMessage = "STATUS_FAILED"
                            }
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
                                        if (!activity.isFinishing) {
                                            withContext(Dispatchers.Main ){
                                                if (dialog.isVisible) {
                                                    dialog.setProgres(progres.toInt())

                                                }
                                            }
                                        }


                                        println(progres)
                                    }
                                }

                            }
                            DownloadManager.STATUS_SUCCESSFUL -> {
                                if (!activity.isFinishing) {
                                    withContext(Dispatchers.Main ){
                                        if (dialog.isVisible) {
                                            dialog.setProgres(100)

                                        }
                                    }
                                }


                                downloadFinished = true
                                errorMessage = "OK"

                            }
                        }
                    }
                    // file:///storage/emulated/0/Android/data/pl.tapo24.twa/files/Download/package/package_main.zip
                }  else {
                    downloadFinished = true
                    errorMessage = "INTERNAL ERROR"
                    isError = true
//                    withContext(Dispatchers.Main) {
//                        dialogLowConnection()
//                    }
                    ACRA.errorReporter.putCustomData("Event at IMPOSSIBLE STATE DOWNLOADER${System.currentTimeMillis()}", State.downloadFinishId.toString())
//                    ACRA.errorReporter.handleSilentException(InternalException(InternalMessage.InternalImpossibleState.message));


                }
                delay(10L)
                cursor.close()
            }
        }.await()
        if (isError) {
           return Result.failure(InternalException(errorMessage))
        }
        return Result.success(errorMessage)

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

                        if (!activity.isFinishing) {
                            if (!dialog.isVisible) {
                                if (childFragmentManager != null && !childFragmentManager.isDestroyed) {
                                    dialog.show(childFragmentManager, "Data")
                                }
                            }
                        }

                        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "pdf/${element.fileName}")
                        // val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "tapo24/pdf/${element.type}/${element.fileName}")
                        file.delete()
                        if (!activity.isFinishing) {
                            withContext(Dispatchers.Main) {
                                delay(10)
                                if (dialog.isVisible) {
                                    dialog.body.value = ("Pobieranie: ${element.name}")

                                }
                            }
                        }

                        async { element.fileName?.let { downloadAsset("pdf" , it) } }.await()

                        async { dataTapoDb.law().insert(element) }.await()

                    }
                    // dialog.dismiss()
                }
                listLaw?.forEach { element ->
                    if(listLawFromServer?.find { el -> el.id == element.id } == null) {
                        if (!activity.isFinishing) {
                            if (!dialog.isVisible) {
                                if (childFragmentManager != null && !childFragmentManager.isDestroyed && !childFragmentManager.isStateSaved) {
                                    dialog.show(childFragmentManager, "Data")
                                }
                            }
                            withContext(Dispatchers.Main) {
                                delay(10)
                                if (dialog.isVisible) {
                                    dialog.body.value = ("Kasowanie starych plików")
                                    dialog.setIndeterminate()
                                }

                            }
                        }

                        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "pdf/${element.fileName}")
                        file.delete()
                        async { dataTapoDb.law().deleteElement(element) }.await()
                    }
                }
                try {
                    dialog.dismiss()
                } catch (_: Throwable) {

                }




            }

            if (!listAsset.isNullOrEmpty()) {
                // update asset

                listAssetFromServer?.forEach {element ->
                    val elementFromDb = listAsset?.find { el -> el.id == element.id }
                    if (element.version > (elementFromDb?.version ?: 0)) {
                        // download updatet or anew dd
                        if (!dialog.isVisible) {
                            if (childFragmentManager != null && !childFragmentManager.isDestroyed) {
                                dialog.show(childFragmentManager, "Data")
                            }
                        }
                        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "${element.path}/${element.name}")
                        // val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "tapo24/pdf/${element.type}/${element.fileName}")
                        file.delete()
                        if (!activity.isFinishing) {
                            withContext(Dispatchers.Main) {
                                delay(10)
                                if (dialog.isVisible) {
                                    dialog.body.value = ("Pobieranie: ${element.name}")

                                }
                            }
                        }

                        var downloadResult: Result<String>? = null
                        async { downloadResult = downloadAsset(element.path , element.name) }.await()
                        downloadResult?.onSuccess {
                            async { tapoDb.assetListDb().insert(element) }.await()
                        }


                    }
                    // dialog.dismiss()
                }
                listAsset?.forEach { element ->
                    if(listAssetFromServer?.find { el -> el.id == element.id } == null) {
                        if (!activity.isFinishing) {
                            if (!dialog.isVisible) {
                                if (childFragmentManager != null && !childFragmentManager.isDestroyed && !childFragmentManager.isStateSaved) {
                                    dialog.show(childFragmentManager, "Data")
                                }
                            }
                            withContext(Dispatchers.Main) {
                                delay(10)
                                if (dialog.isVisible) {
                                    dialog.body.value = ("Kasowanie starych plików")
                                    dialog.setIndeterminate()
                                }

                            }
                        }

                        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "${element.path}/${element.name}")
                        file.delete()
                        async { tapoDb.assetListDb().deleteElement(element) }.await()
                    }
                }
                if (dialog.isVisible && !activity.isFinishing &&!activity.isDestroyed && childFragmentManager != null && !childFragmentManager.isDestroyed && !childFragmentManager.isStateSaved) {
                    dialog.dismiss()

                }

            } else {
                activity.requestedOrientation = (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                // downoload all because init
                withContext(Dispatchers.Main) {
                    delay(10)
                    // MAT24-35 java.lang.NullPointerException
                    if (!activity.isFinishing) {
                        if (!dialog.isVisible) {
                            if (childFragmentManager != null && !childFragmentManager.isDestroyed && !childFragmentManager.isStateSaved) {
                                dialog.show(childFragmentManager, "Data")
                                delay(10)
                                // MAT24-35 java.lang.NullPointerException
                                dialog.body.value = ("Pobieranie głównej paczki")
                            }
                        }
                    }


                }
                var response: Result<String>? = null
                //downloadAsset("package", "package_main.zip")
                async { response = Downloader().downloadAsset("package", "package_main.zip", context) }.await()
                response?.onSuccess {
                    delay(500)
                    var steps = 0
                    while (State.downloadFinishId == 0L && steps < 10) {
                        delay(500)
                        steps += 1
                    }

                    val stat = StatFs(Environment.getExternalStorageDirectory().path)
                    val bytesAvailable: Long
                    bytesAvailable = if (Build.VERSION.SDK_INT >=
                        Build.VERSION_CODES.JELLY_BEAN_MR2
                    ) {
                        stat.blockSizeLong * stat.availableBlocksLong
                    } else {
                        stat.blockSize.toLong() * stat.availableBlocks.toLong()
                    }
                    val megAvailable = bytesAvailable / (1024 * 1024)
                    Log.e("", "Available MB : $megAvailable")


                    ACRA.errorReporter.putCustomData("Free Space is: ", megAvailable.toString())
                    ACRA.errorReporter.putCustomData("Steps is ", steps.toString())
                    ACRA.errorReporter.putCustomData("download id is ", State.downloadFinishId.toString())
                    val file = File(
                        context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                        "package/package_main.zip"
                    )
                    if (!activity.isFinishing) {
                        withContext(Dispatchers.Main) {
                            if (dialog.isVisible) {
                                dialog.body.value =
                                    ("Dekompresja głównej paczki, w tym czasie możesz iśc na kawę ;)")
                                dialog.setIndeterminate()
                            }

                        }
                    }
                    var countRetries: Int = 0
                    var successExtract: Boolean = false
                    var lastEx: Throwable? = null
                    while (countRetries < 5 && !successExtract) {
                        successExtract = true
                        countRetries += 1
                        try {
                            context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                                ?.toURI()?.path?.let { PackageExtractor.unzip(it, file) }

                            file.delete()
                            if (!activity.isFinishing) {
                                withContext(Dispatchers.Main) {
                                    if (dialog.isVisible) {
                                        dialog.setDone()

                                    }
                                    delay(1000)
                                    if (dialog.isVisible && !activity.isFinishing && !activity.isDestroyed
                                        && childFragmentManager !=null && !childFragmentManager.isStateSaved) {
                                        try {
                                            dialog.dismiss()
                                        } catch (ex: IllegalStateException) {
                                            ACRA.errorReporter.handleSilentException(ex)
                                        }


                                    }
                                }
                            }

                            listAssetFromServer?.let { tapoDb.assetListDb().insertList(it) }
                            listLawFromServer?.let { dataTapoDb.law().insertList(it) }
                        } catch (ex: Throwable) {
                            successExtract = false
                            lastEx = ex


                        }
                        delay(5000)
                    }
                    if (!successExtract) {
                        ACRA.errorReporter.handleSilentException(lastEx)
                        withContext(Dispatchers.Main) {
                            dialogErrorDuringMainPackage()
                        }
                    }

                }
                response?.onFailure {
                    withContext(Dispatchers.Main) {
                        try {
                            dialogErrorDuringMainPackage()

                        } catch (ex: Throwable) {
                            ACRA.errorReporter.handleSilentException(ex)
                        }
                    }

                }
                if (response == null) {
                    withContext(Dispatchers.Main) {
                        try {
                            dialogErrorDuringMainPackage()

                        } catch (ex: Throwable) {
                            ACRA.errorReporter.handleSilentException(ex)
                        }
                    }

                }


                // force clear other package file
                val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "package")
                if (directory.isDirectory && directory.listFiles() != null){
                    for (listFile in directory.listFiles()!!) {
                        listFile.delete()
                    }
                }
                directory.delete()
                activity.requestedOrientation = (ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR)
            }

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
