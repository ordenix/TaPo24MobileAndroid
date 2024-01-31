package pl.tapo24.twa.updater

import android.app.DownloadManager
import android.net.Uri
import android.os.Environment
import android.content.Context
import kotlinx.coroutines.*
import org.acra.ACRA
import pl.tapo24.twa.data.State
import pl.tapo24.twa.exceptions.InternalException
import java.io.File
import javax.inject.Inject

class Downloader {

    suspend fun deleteAsset(type: String, name: String, context: Context, isPublicStorage: Boolean = false) {
        MainScope().async {
            if (isPublicStorage) {
                val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "tapo24Don'tDelete/$type/$name")
                file.delete()
            } else {
                val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "$type/$name")
                file.delete()
            }
        }.await()
    }
    suspend fun downloadAsset(type: String, name: String, context: Context, isPublicStorage: Boolean = false): Result<String> {
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
            if (isPublicStorage) {
                request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "tapo24Don'tDelete/$type/$name"
                )
            } else {
                request.setDestinationInExternalFilesDir(
                    context,
                    Environment.DIRECTORY_DOWNLOADS,
                    "$type/$name"
                )
            }

            val downloadManager = context.getSystemService(DownloadManager::class.java)!!
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

                                            withContext(Dispatchers.Main ){
                                                State.dialogDownloadFileProgress.value = progres.toInt()
                                            }
                                    }
                                }

                            }
                            DownloadManager.STATUS_SUCCESSFUL -> {
                                withContext(Dispatchers.Main ){
                                    State.dialogDownloadFileProgress.value = 100

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
                    ACRA.errorReporter.putCustomData("Event at IMPOSSIBLE STATE DOWNLOADER${System.currentTimeMillis()}", State.downloadFinishId.toString())

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

}