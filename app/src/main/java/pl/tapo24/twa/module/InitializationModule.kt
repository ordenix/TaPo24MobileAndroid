package pl.tapo24.twa.module

import android.content.Context
import android.content.SharedPreferences
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.*
import pl.tapo24.twa.R
import pl.tapo24.twa.data.*
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Setting
import pl.tapo24.twa.infrastructure.NetworkClient
import pl.tapo24.twa.updater.DialogDataUpdater
import javax.inject.Inject

class InitializationModule @Inject constructor(private val tapoDb: TapoDb, private val networkClient: NetworkClient, private val context: Context){
    fun setNetworkType() {

    }

    fun initializeDialogForDataUpdater(activity: AppCompatActivity, supportFragmentManager:FragmentManager ) {
        val dialog = MaterialAlertDialogBuilder(activity)
            .setTitle("Pobieranie danych")
            .setMessage("Proszę czekać")
            // .setCancelable(false)
            .create()

        State.dialogDownloadMessage.observe(activity, Observer {
            if (it.isNotEmpty()) {
                if (!supportFragmentManager.isStateSaved && !supportFragmentManager.isDestroyed && !activity.isFinishing && !activity.isDestroyed) {
                    if (dialog.isShowing) {
                        dialog.setMessage(it)
                    } else {
                        dialog.setMessage(it)
                        dialog.show()
                    }
                }
            } else {
                if (dialog.isShowing && !supportFragmentManager.isStateSaved && !supportFragmentManager.isDestroyed && !activity.isFinishing && !activity.isDestroyed) {
                    dialog.dismiss()
                }
            }
        })
        State.setIndeterminate.observe(activity, Observer {
            if (it &&
                State.dialogUpdater.isVisible
                && !supportFragmentManager.isStateSaved
                && !supportFragmentManager.isDestroyed && !activity.isFinishing && !activity.isDestroyed) {
                State.dialogUpdater.setIndeterminate()

            }
        })

        State.dialogDownloadFileProgress.observe(activity, Observer {
            if (it != null) {
                if (!supportFragmentManager.isStateSaved && !supportFragmentManager.isDestroyed && !activity.isFinishing && !activity.isDestroyed) {
                    if (State.dialogUpdater.isVisible) {
                        State.dialogUpdater.setBody(State.dialogDownloadFileMessage)
                        State.dialogUpdater.setProgres(it)
                    } else {
                        MainScope().launch(Dispatchers.IO) {
                            withContext(Dispatchers.Main) {
                                if (!State.dialogUpdater.isVisible && !State.dialogUpdater.isAdded) {
                                    State.dialogUpdater.show(supportFragmentManager.beginTransaction().remove(State.dialogUpdater), "dialogUpdater")
                                }


                            }
                        }

                    }
                }
            } else {
                if (State.dialogUpdater.isVisible && !supportFragmentManager.isStateSaved
                    && !supportFragmentManager.isDestroyed && !activity.isFinishing && !activity.isDestroyed) {
                    State.dialogUpdater.setDone()
                    MainScope().launch(Dispatchers.IO) {
                        delay(1000)
                        withContext(Dispatchers.Main) {
                            State.dialogUpdater.dismiss()
                        }


                    }

                }
            }
        })
    }

    fun setThemeObserver(activity: AppCompatActivity
    ) {
        State.settingFontBoldTariff.observe(activity, Observer {
            if (it) {
                activity.theme.applyStyle(R.style.boldFont, true)
            } else {
                activity.theme.applyStyle(R.style.nonBoldFont, true)
            }
        })
        State.settingFontBoldMain.observe(activity, Observer {
            if (it) {
                activity.theme.applyStyle(R.style.boldFontMain, true)
            } else {
                activity.theme.applyStyle(R.style.nonBoldFontMain, true)
            }
        })
    }

    fun getThemeParameters() {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            "ThemePref",
            Context.MODE_PRIVATE
        )
        MainScope().async(Dispatchers.IO) {
            var settingFontBoldTariff : Setting? = null
            var settingFontBoldMain : Setting? = null
            async { settingFontBoldTariff = tapoDb.settingDb().getSettingByName("settingFontBoldTariff") }.await()
            async { settingFontBoldMain = tapoDb.settingDb().getSettingByName("settingFontBoldMain") }.await()
            if (settingFontBoldTariff == null) {
                async {
                    val setting: Setting = Setting("settingFontBoldTariff","", state = true)
                    tapoDb.settingDb().insert(setting)
                }.await()
                withContext(Dispatchers.Main) {
                    State.settingFontBoldTariff.value = true
                }
            } else {
                withContext(Dispatchers.Main) {
                    State.settingFontBoldTariff.value = settingFontBoldTariff!!.state
                }
            }
            if (settingFontBoldMain == null) {
                async {
                    val setting: Setting = Setting("settingFontBoldMain","", state = true)
                    tapoDb.settingDb().insert(setting)
                }.await()
                withContext(Dispatchers.Main) {
                    State.settingFontBoldMain.value = true
                }
            } else {
                withContext(Dispatchers.Main) {
                    State.settingFontBoldMain.value = settingFontBoldMain!!.state
                }
            }
            val funeralFromShared: Boolean = sharedPreferences.getBoolean("funeralTheme", false)
            State.funeralTheme = funeralFromShared

        }
    }

    fun getUid() {
        MainScope().launch(Dispatchers.IO) {
            var settingUid: Setting? = null
            async { settingUid = tapoDb.settingDb().getSettingByName("uid") }.await()

            if (settingUid == null) {
                async {
                    val response = networkClient.getUid()
                    response.onSuccess {
                        val setting = Setting("uid",it.uid!!)
                        tapoDb.settingDb().insert(setting)
                    }

                }.await()
            } else {
                State.uid = settingUid!!.value
            }
        }

    }

    fun getSetting() {
        MainScope().launch(Dispatchers.IO) {
            var settingEnvironment : Setting? = null
            var settingEngine : Setting? = null
            async { settingEnvironment = tapoDb.settingDb().getSettingByName("settingEnvironment") }.await()
            async { settingEngine = tapoDb.settingDb().getSettingByName("settingEngine") }.await()

            if (settingEnvironment == null) {
                State.environmentType = EnvironmentType.Master
                async {
                    val setting: Setting = Setting("settingEnvironment","", EnvironmentType.Master.ordinal)
                    tapoDb.settingDb().insert(setting)
                }.await()
                State.environmentType = EnvironmentType.Master

            } else {
                State.environmentType = EnvironmentType.values()[settingEnvironment!!.count]
            }
            if (settingEngine == null) {
                State.enginesType = EnginesType.New
                async {
                    val setting: Setting = Setting("settingEngine","", EnginesType.New.ordinal)
                    tapoDb.settingDb().insert(setting)
                }.await()
                State.enginesType = EnginesType.New
            } else {
                State.enginesType = EnginesType.values()[settingEngine!!.count]
            }
            val showNotifyForTariffIcon = async { tapoDb.settingDb().getSettingByName("showNotifyForTariffIcon") }.await()
            if (showNotifyForTariffIcon == null){
                async {
                    val setting: Setting = Setting("showNotifyForTariffIcon","", state = true)
                    tapoDb.settingDb().insert(setting)
                }.await()
                State.showNotifyForTariffIcon = true
            } else {
                State.showNotifyForTariffIcon = showNotifyForTariffIcon!!.state
            }
        }


    }
    fun saveInitNotification() {
        MainScope().launch(Dispatchers.IO){
            val settingToInsert = Setting("NotificationInit","",0,state = true)
            async { tapoDb.settingDb().insert(settingToInsert) }.await()
        }
    }

    fun returnStyleTheme(funeral: Boolean = false, themeKey: String = "default"): ThemeTypes {
        return if (!funeral) {
            val themeType: ThemeTypes? = ThemeTypes.values().find { element -> element.type == themeKey }
            themeType ?: ThemeTypes.Default
        } else {
            ThemeTypes.FuneralTheme
        }

    }


    fun returnFontStyle( themeKey: String = "Itim"): FontTypes {
        val fontType: FontTypes? = FontTypes.values().find { element -> element.type == themeKey }
        return fontType ?: FontTypes.itim
    }


}