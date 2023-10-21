package pl.tapo24.twa.ui.settings

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.updater.DataUpdater
import pl.tapo24.twa.data.EnginesType
import pl.tapo24.twa.data.EnvironmentType
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

    init {
        viewModelScope.launch(Dispatchers.IO) {
            var settingEnvironment : Setting? = null
            var settingEngine : Setting? = null
            async { settingEnvironment = tapoDb.settingDb().getSettingByName("settingEnvironment") }.await()
            async { settingEngine = tapoDb.settingDb().getSettingByName("settingEngine") }.await()
            withContext(Dispatchers.Main ){
                environment.value = EnvironmentType.values()[settingEnvironment!!.count]
                engine.value = EnginesType.values()[settingEngine!!.count]
            }

        }
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
            if (envSettingToDb != null) {
                tapoDb.settingDb().insert(envSettingToDb)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            if (engSettingToDb != null) {
                tapoDb.settingDb().insert(engSettingToDb)
            }
        }
    }

//    private val _text = MutableLiveData<String>().apply {
//        value = "Thisdsfsdf sdf df  is seting"
//    }
//    val text: LiveData<String> = _text
}