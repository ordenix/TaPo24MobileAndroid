package pl.tapo24.twa.ui.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.adapter.SettingThemeAdapter
import pl.tapo24.twa.data.*
import pl.tapo24.twa.updater.DataUpdater
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Setting
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.infrastructure.NetworkClient
import pl.tapo24.twa.infrastructure.NetworkClientRegister
import javax.inject.Inject
@HiltViewModel
class SettingsViewModel@Inject constructor(
    private val tapoDb: TapoDb,
    private val networkClient: NetworkClient,
    private val dataTapoDb: DataTapoDb,
    private val networkClientRegister: NetworkClientRegister
) : ViewModel() {
    val environment = MutableLiveData<EnvironmentType>(EnvironmentType.Master)
    val engine = MutableLiveData<EnginesType>(EnginesType.New)
    val connectionType = MutableLiveData<String>("WiFi")

    val fontType = MutableLiveData<FontTypes>(FontTypes.itim)
    val themeType = MutableLiveData<ThemeTypes>(ThemeTypes.Default)

    val settingFontBoldMain = MutableLiveData<Boolean>(true)
    val settingFontBoldTariff = MutableLiveData<Boolean>(true)
    lateinit var adapter: SettingThemeAdapter
    val themeTypesDataList: MutableLiveData<List<ThemeTypesData>> = MutableLiveData()
//settingNetwork
    // WiFi
    // All
    init {
        fontType.value = State.settingFont.value
        themeType.value = State.settingTheme.value
        settingFontBoldMain.value = State.settingFontBoldMain.value
        settingFontBoldTariff.value = State.settingFontBoldTariff.value
        viewModelScope.launch(Dispatchers.IO) {
            var settingEnvironment : Setting? = null
            var settingEngine : Setting? = null
            var settingConnection : Setting? = null
            async { settingEnvironment = tapoDb.settingDb().getSettingByName("settingEnvironment") }.await()
            async { settingEngine = tapoDb.settingDb().getSettingByName("settingEngine") }.await()
            async { settingConnection = tapoDb.settingDb().getSettingByName("settingNetwork") }.await()
            withContext(Dispatchers.Main ){
                environment.value = EnvironmentType.values()[settingEnvironment!!.count]
                engine.value = EnginesType.values()[settingEngine!!.count]
                connectionType.value = settingConnection!!.value // If catch here null refactor it add to init on start application
            }

        }
        prepareThemeList()
    }

    private fun prepareThemeList() {
        val list = mutableListOf<ThemeTypesData>()
        ThemeTypes.values().forEach {element->
            if (!element.isHiddenInList) {
                var selected = false
                if (element == State.settingTheme.value) {
                    selected = true
                }
                list.add(ThemeTypesData(element, selected))
            }
        }
        themeTypesDataList.value = list
    }
    fun selectTheme(theme: ThemeTypes) {
        State.settingTheme.value = theme
    }
    fun getData(context: Context){
        environment.value?.let { networkClient.rebuild(it.url) }
        environment.value?.let { networkClientRegister.rebuild(it.url) }
        val dataupdate = DataUpdater(tapoDb,dataTapoDb,networkClient,context)
        dataupdate.deleteData()
        dataupdate.getData()
    }
    fun saveSettings () {
        var envSettingToDb: Setting? = Setting("settingEnvironment")
        var engSettingToDb: Setting? = Setting("settingEngine")
        var connSettingToDb: Setting? = Setting("settingNetwork")
        connSettingToDb = Setting("settingNetwork",connectionType.value!!,0)
        val fontSettingToDb: Setting = Setting("settingFont","", fontType.value!!.ordinal)
        val themeSettingToDb: Setting = Setting("settingTheme","", themeType.value!!.ordinal)
        val boldMainSettingToDb: Setting = Setting("settingFontBoldMain","", state = settingFontBoldMain.value!!)
        val boldTariffSettingToDb: Setting = Setting("settingFontBoldTariff","", state = settingFontBoldTariff.value!!)
        State.settingFont.value = fontType.value
        State.settingTheme.value = themeType.value!!
        State.settingFontBoldTariff.value = settingFontBoldTariff.value
        State.settingFontBoldMain.value = settingFontBoldMain.value
        when (engine.value) {
            EnginesType.Old -> engSettingToDb = Setting("settingEngine","",0)
            EnginesType.New -> engSettingToDb = Setting("settingEngine","",1)
            else -> {}
        }
        when (environment.value) {
            EnvironmentType.Beta -> envSettingToDb = Setting("settingEnvironment","",0)
            EnvironmentType.Feature -> envSettingToDb = Setting("settingEnvironment","",1)
            EnvironmentType.Master -> envSettingToDb = Setting("settingEnvironment","",2)
            else -> {}
        }
        viewModelScope.launch(Dispatchers.IO) {
            if (engSettingToDb != null) {
                tapoDb.settingDb().insert(engSettingToDb)
            }
            if (envSettingToDb != null) {
                tapoDb.settingDb().insert(envSettingToDb)
            }
            if (connSettingToDb != null) {
                tapoDb.settingDb().insert(connSettingToDb)
            }
            if (fontSettingToDb != null) {
                tapoDb.settingDb().insert(fontSettingToDb)
            }
            tapoDb.settingDb().insert(themeSettingToDb)
            tapoDb.settingDb().insert(boldMainSettingToDb)
            tapoDb.settingDb().insert(boldTariffSettingToDb)
        }
    }

//    private val _text = MutableLiveData<String>().apply {
//        value = "Thisdsfsdf sdf df  is seting"
//    }
//    val text: LiveData<String> = _text
}