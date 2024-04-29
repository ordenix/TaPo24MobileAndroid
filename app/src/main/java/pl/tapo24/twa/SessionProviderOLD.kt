//package pl.tapo24.twa
//
//import android.content.Context
//import android.content.SharedPreferences
//import android.os.Build
//import androidx.work.*
//import com.google.android.material.dialog.MaterialAlertDialogBuilder
//import com.google.android.material.snackbar.Snackbar
//import dagger.hilt.EntryPoint
//import dagger.hilt.InstallIn
//import dagger.hilt.android.EntryPointAccessors
//import dagger.hilt.components.SingletonComponent
//import kotlinx.coroutines.*
//import kotlinx.serialization.json.Json
//import pl.tapo24.twa.data.State
//import pl.tapo24.twa.data.TokenDecodeData
//import java.util.Base64
//import kotlinx.serialization.decodeFromString
//import pl.tapo24.twa.data.NetworkTypes
//import pl.tapo24.twa.data.login.DataUser
//import pl.tapo24.twa.data.login.ToLoginData
//import pl.tapo24.twa.db.TapoDb
//import pl.tapo24.twa.db.entity.Setting
//import pl.tapo24.twa.exceptions.HttpException
//import pl.tapo24.twa.exceptions.InternalException
//import pl.tapo24.twa.exceptions.InternalMessage
//import pl.tapo24.twa.infrastructure.NetworkClient
//import pl.tapo24.twa.succes.HttpSuccessMessage
//import pl.tapo24.twa.updater.LawUpdater
//import pl.tapo24.twa.worker.RegenerateJwtTokenWorker
//import pl.tapo24.twa.worker.UpdateWorker
//import java.util.concurrent.TimeUnit
//import javax.inject.Inject
//
//class SessionProvider @Inject constructor(private var tapoDb: TapoDb, private var networkClient: NetworkClient, private var context: Context,)
//
// {
//
//     private lateinit var sharedPreferences: SharedPreferences
//
//     val workManager: WorkManager = WorkManager.getInstance(context)
//     val constraints = Constraints.Builder()
//         .setRequiredNetworkType(NetworkType.CONNECTED)
//         .build()
//
//
//
//
//}