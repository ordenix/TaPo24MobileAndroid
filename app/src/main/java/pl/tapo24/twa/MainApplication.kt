package pl.tapo24.twa


import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.work.*
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.*
import org.acra.ACRA
import org.acra.config.httpSender
import org.acra.config.toast
import org.acra.data.StringFormat
import org.acra.ktx.initAcra
import org.acra.sender.HttpSender
import pl.tapo24.twa.data.State
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Setting
import pl.tapo24.twa.module.InitializationModule
import pl.tapo24.twa.updater.InitPackageDownloader
import pl.tapo24.twa.worker.DataBaseUpdateWorker
import pl.tapo24.twa.worker.MourningWorker
import pl.tapo24.twa.worker.NonPremiumLimitationWorker
import pl.tapo24.twa.worker.UpdateWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class MainApplication: Application() {
    @Inject
    lateinit var tapoDb: TapoDb
    @Inject
    lateinit var initializationModule: InitializationModule
    @Inject
    lateinit var initPackageDownloader: InitPackageDownloader


    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        initializationModule.getUid()
        initializationModule.getSetting()
        try {
            val workManager = WorkManager.getInstance(this)
            val constraints = Constraints.Builder()
                //.setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val workRequest = PeriodicWorkRequestBuilder<MourningWorker>(5, TimeUnit.HOURS)
                .setConstraints(constraints)
                .addTag("Mourning")
                .build()
            workManager.enqueueUniquePeriodicWork("Mourning", ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE, workRequest)
            val constraints2 = Constraints.Builder()
                .build()
            val workRequest2 = PeriodicWorkRequestBuilder<NonPremiumLimitationWorker>(10, TimeUnit.DAYS)
                .setConstraints(constraints2)
                .setInitialDelay(10, TimeUnit.DAYS)
                .addTag("ShowNotifyForTariffIcon")
                .build()
            workManager.enqueueUniquePeriodicWork("ShowNotifyForTariffIcon", ExistingPeriodicWorkPolicy.KEEP, workRequest2)
            val constraints3 = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val workRequest3 = PeriodicWorkRequestBuilder<DataBaseUpdateWorker>(10, TimeUnit.HOURS)
                .setConstraints(constraints3)
                .addTag("DataUpdate")
                .build()
            workManager.enqueueUniquePeriodicWork("DataUpdate", ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE, workRequest3)
            MainScope().launch(Dispatchers.IO) {
                var settingNetwork: Setting? = null
                async { settingNetwork = tapoDb.settingDb().getSettingByName("settingNetwork") }.await()
                // val settingInitializePackage = async { tapoDb.settingDb().getSettingByName("settingInitializePackage") }.await()
                if (settingNetwork != null) {
                    State.networkType = settingNetwork!!.value
                    val constraints4 = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                    val workRequest4 = PeriodicWorkRequestBuilder<UpdateWorker>(10, TimeUnit.HOURS)
                        .setConstraints(constraints4)
                        .addTag("DataLawAndAssetUpdate")
                    .setInitialDelay(5, TimeUnit.SECONDS)
                        .build()
                    workManager.enqueueUniquePeriodicWork("DataLawAndAssetUpdate", ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE, workRequest4)
//                }

                }
            }
        } catch (e: Exception) {
            try {
                ACRA.errorReporter.handleSilentException(e)
            } catch (e: Exception) {
                println(e)
            }
        }






    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        if   (!BuildConfig.DEBUG) {
            initAcra {
                reportFormat = StringFormat.JSON
                httpSender {
                    uri = "https://acra.tapo24.pl/report" /*best guess, you may need to adjust this*/
                    basicAuthLogin = "LEEPculNAAaeiJgr"
                    basicAuthPassword = "7kaqOKxTeEuWP5tm"
                    httpMethod = HttpSender.Method.POST
                }
//            dialog {
//                //required
//                text = "Przykro nam, że spotkało to właśnie Ciebie"
//                //optional, enables the dialog title
//                title = "Oj awaria :("
//                //defaults to android.R.string.ok
//                positiveButtonText = "getString(R.string.dialog_positive)"
//                //defaults to android.R.string.cancel
//                //negativeButtonText = "getString(R.string.dialog_negative)"
//                //optional, enables the comment input
//                commentPrompt = "getString(R.string.dialog_comment)"
//                //optional, enables the email input
//                //emailPrompt =" getString(R.string.dialog_email)"
//                //defaults to android.R.drawable.ic_dialog_alert
//                //resIcon = "R.drawable.dialog_icon"
//                //optional, defaults to @android:style/Theme.Dialog
//               // resTheme = "R.style.dialog_theme"
//                //allows other customization
//                //reportDialogClass = MyCustomDialog::class.java
//            }

                toast {
                    //required
                    text = "O Jezus Maria zdarzyła się awaria:( Programista pracuje aby usunąć błąd"
                    //defaults to Toast.LENGTH_LONG
                    length = Toast.LENGTH_LONG
                }
            }
        }


    }
}