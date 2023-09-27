package pl.tapo24.twa.updater

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.FragmentManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pl.tapo24.twa.data.State
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.AppVersion
import pl.tapo24.twa.infrastructure.NetworkClient
import kotlin.system.exitProcess

class CheckVersion(
    val tapoDb: TapoDb,
    val networkClient: NetworkClient,
    val context: Context
) {

    fun checkVersion() {
        MainScope().launch(Dispatchers.IO) {
            var appVersion: List<AppVersion>? = null
            var appVersionsFromDb: List<AppVersion>? = null
            async { appVersionsFromDb = tapoDb.appVersionDb().getAll() }.await()
            async {
                val response =  networkClient.getAppVersionData()
                response.onSuccess {
                    appVersion = it
                }

            }.await()
            async {
                if (appVersion != null) {
                    appVersion!!.forEach { element ->
                        if (appVersionsFromDb!!.find { element2-> element2.id == element.id } == null) {
                            tapoDb.appVersionDb().insert(element)
                        }
                    }
                }
            }.await()

            var lastAppFromDb:AppVersion? = null
            async { lastAppFromDb = tapoDb.appVersionDb().getLast() }.await()
            if (lastAppFromDb != null) {
                if (!lastAppFromDb!!.dismissed && lastAppFromDb!!.versionCode > State.versionCode) {
                    if (lastAppFromDb!!.critical) {
                        // is critical
                        showDialogCritical(lastAppFromDb!!.whatChange)
                    } else {
                        // is normal
                        showDialogNormal(lastAppFromDb!!)
                    }
                }
            }
        }

    }
    private fun changeDismissed(item: AppVersion) {
        MainScope().launch(Dispatchers.IO) {
            item.dismissed = true
            async { tapoDb.appVersionDb().insert(item) }.await()
        }
    }

    private fun showDialogCritical(message: String) {
        MainScope().launch(Dispatchers.Main) {
            MaterialAlertDialogBuilder(context)
                .setTitle("Aktualizacja o wysokim priorytecie")
                .setCancelable(false)
                .setMessage("$message Zamknięcie tego komunikatu spowoduje zakończenie aplikacji!!")
                .setNeutralButton("Zakończ działanie aplikacji") { dialog, which ->
                    // Respond to neutral button press
                    exitProcess(0)
                }
                .setPositiveButton("Otwórz sklep") { dialog, which ->
                    openStore()
                    exitProcess(0)
                }
                .show()
        }

    }

    private fun showDialogNormal(item: AppVersion) {
        MainScope().launch(Dispatchers.Main) {
            MaterialAlertDialogBuilder(context)
                .setTitle("Aktualizacja")
                .setMessage(item.whatChange)
                .setNeutralButton("Zamknij") { dialog, which ->
                    // Respond to neutral button press
                    changeDismissed(item)
                    dialog.dismiss()
                }
                .setPositiveButton("Otwórz sklep") { dialog, which ->
                    openStore()
                }
                .show()
        }

    }

    private fun openStore() {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=pl.tapo24.twa")))
        } catch (e: ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=pl.tapo24.twa")))
        }
    }
}