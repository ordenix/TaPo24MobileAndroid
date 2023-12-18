package pl.tapo24.twa

import kotlinx.coroutines.*
import pl.tapo24.twa.data.*
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Setting
import pl.tapo24.twa.infrastructure.NetworkClient
import javax.inject.Inject

class InitializationModule @Inject constructor(private val tapoDb: TapoDb, private val networkClient: NetworkClient) {
    fun setNetworkType() {

    }

    fun getThemeParameters() {
        MainScope().async(Dispatchers.IO) {
            var settingFont : Setting? = null
            var settingFontBoldTariff : Setting? = null
            var settingFontBoldMain : Setting? = null
            async { settingFont = tapoDb.settingDb().getSettingByName("settingFont") }.await()
            async { settingFontBoldTariff = tapoDb.settingDb().getSettingByName("settingFontBoldTariff") }.await()
            async { settingFontBoldMain = tapoDb.settingDb().getSettingByName("settingFontBoldMain") }.await()
            if (settingFont == null) {
                async {
                    val setting: Setting = Setting("settingFont","", FontTypes.itim.ordinal)
                    tapoDb.settingDb().insert(setting)
                }.await()
                withContext(Dispatchers.Main) {
                    State.settingFont.value = FontTypes.itim
                }
            } else {
                withContext(Dispatchers.Main) {
                    State.settingFont.value = FontTypes.values()[settingFont!!.count]
                }
            }
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
        }


    }
    fun saveInitNotification() {
        MainScope().launch(Dispatchers.IO){
            val settingToInsert = Setting("NotificationInit","",0,state = true)
            async { tapoDb.settingDb().insert(settingToInsert) }.await()
        }
    }

    fun returnStyleTheme(funeral: String = "false", themeKey: String = "default"): ThemeTypes {
        return if (funeral == "false") {
            val themeType: ThemeTypes? = ThemeTypes.values().find { element -> element.type == themeKey }
            themeType ?: ThemeTypes.Default
        } else {
            ThemeTypes.FuneralTheme
        }

    }


}