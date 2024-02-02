package pl.tapo24.twa.updater


import android.content.Context
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import org.acra.ACRA
import pl.tapo24.twa.data.State
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Setting
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.infrastructure.NetworkClient
import pl.tapo24.twa.worker.UpdateWorker
import java.io.File
import javax.inject.Inject

class InitPackageDownloader @Inject constructor(
    @ApplicationContext private val context: Context,
    private val networkClient: NetworkClient,
    private val tapoDb: TapoDb,
    private val dataTapoDb: DataTapoDb
)

{
    val namePackage: String = "package_mainold.zip"

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface LawUpdaterProviderEntryPoint {
        fun lawUpdater(): LawUpdater
    }



    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface AssetUpdaterProviderEntryPoint {
        fun assetUpdater(): AssetUpdater
    }

    suspend fun downloadInitPackage(isPublicStorage: Boolean): Result<String>? {
        TODO("change package name to new one")
        var result: Result<String>?  = null
        MainScope().async(Dispatchers.IO) {
            downloadMainPackage(isPublicStorage)
                ?.onSuccess {
                   // insertDataToDb()
                    result = Result.success("Success download")
                    withContext(Dispatchers.Main) {
                        State.dialogDownloadFileMessage = "Sukces"
                        State.dialogDownloadFileProgress.value  = null
                    }
                }
                ?.onFailure {
                    ACRA.errorReporter.handleSilentException(it)
                    result = Result.failure(it)
//                    isPublicStorage = true
//                    if (it.message?.contains("Permission denied") ?: false) {
//                        downloadMainPackage(true)
//                            ?.onSuccess {  }
//                            ?.onFailure {
//                                when (it) {
//                                    is java.io.FileNotFoundException -> {
//                                        ACRA.errorReporter.handleSilentException(it)
//                                        withContext(Dispatchers.Main) {
//                                            State.dialogDownloadFileMessage = "Błąd przy pobraniu paczki głównej, nastąpi pobranie proceduralne (może to potrwać chwilę dłużej)"
//                                            State.dialogDownloadFileProgress.value  = 0
//                                            State.setIndeterminate.value = true
//                                        }
//                                        delay(4000)
////                                        val hiltEntryPoint = EntryPointAccessors.fromApplication(context,
////                                            LawUpdaterProviderEntryPoint::class.java)
////                                        hiltEntryPoint.lawUpdater().update()
////                                        val hiltEntryPoint2 = EntryPointAccessors.fromApplication(context,
////                                            AssetUpdaterProviderEntryPoint::class.java)
////                                        hiltEntryPoint2.assetUpdater().update()
//                                    }
//                                    else -> {
//
//                                    }
//                                }
//                            }
//                    }

                }
            clearPackageFolders()

        }.await()
    return result

    }
    fun saveSettings(isPublicStorage: Boolean, choseConnection: String) {
        MainScope().launch(Dispatchers.Main) {
            State.networkType = choseConnection
        }
        MainScope().launch(Dispatchers.IO) {
            tapoDb.settingDb().insert(Setting("settingInitializePackage", state = true))
            tapoDb.settingDb().insert(Setting("publicStorage", state = isPublicStorage))
            tapoDb.settingDb().insert(Setting("settingNetwork", choseConnection))
        }
    }

    private suspend fun clearPackageFolders() {
        MainScope().async(Dispatchers.IO) {
            val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "package")
            val directory2 = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "tapo24Don'tDelete/package")
            try {
                if (directory.isDirectory && directory.listFiles() != null){
                    for (listFile in directory.listFiles()!!) {
                        listFile.delete()
                    }
                }
                directory.delete()
                if (directory2.isDirectory && directory2.listFiles() != null){
                    for (listFile in directory2.listFiles()!!) {
                        listFile.delete()
                    }
                }
                directory2.delete()
            } catch (e: Exception) {
                println("ERROR")
            }

        }.await()
    }

    private suspend fun downloadMainPackage(isPublicStorage: Boolean = false): Result<String>? {
        var result: Result<String>? = null
        MainScope().async(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                State.dialogDownloadFileMessage = if (isPublicStorage) {
                    "Ponowne pobieranie głównej paczki"
                } else {
                    "Pobieranie głównej paczki"
                }
                State.dialogDownloadFileProgress.value  = 0
            }
            Downloader().downloadAsset("package", namePackage, context, isPublicStorage)
                .onSuccess {
                    val file: File = if (isPublicStorage) {
                        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "tapo24Don'tDelete/package/$namePackage")

                    } else {
                        File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "package/$namePackage")
                    }
                    val path = if (isPublicStorage) {
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            ?.toURI()?.path + "tapo24Don'tDelete/"
                    } else {
                        context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                            ?.toURI()?.path
                    }
                    withContext(Dispatchers.Main) {
                        State.dialogDownloadFileMessage = "Dekompresja głównej paczki, w tym czasie możesz iśc na kawę ;)"
                        State.dialogDownloadFileProgress.value  = 0
                        State.setIndeterminate.value = true
                    }
                    if (file.exists()) {
                       if (path != null) {
                           PackageExtractor.unzip(path, file).onSuccess {
                               result = Result.success("OK")
                           }
                               .onFailure {
                                   result = Result.failure(it)
                               }

                       } else {
                           result = Result.failure(Exception("Path is null"))
                       }
                    } else {
                        result = Result.failure(Exception("File not exists"))
                    }


            }
                .onFailure {
                    result = Result.failure(it)
                }
        }.await()
        return result
    }


    fun insertDataToDb() {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(context,
            LawUpdaterProviderEntryPoint::class.java)
        hiltEntryPoint.lawUpdater().initUpdate()
        val hiltEntryPoint2 = EntryPointAccessors.fromApplication(context,
            AssetUpdaterProviderEntryPoint::class.java)
        hiltEntryPoint2.assetUpdater().initUpdate()

    }

}