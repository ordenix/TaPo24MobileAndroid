package pl.tapo24.twa.data

import androidx.lifecycle.MutableLiveData
import pl.tapo24.twa.BuildConfig
import pl.tapo24.twa.R
import pl.tapo24.twa.db.entity.CustomCategory
import pl.tapo24.twa.db.entity.MapCategory
import pl.tapo24.twa.updater.DialogDataUpdater

object State {
    val dialogUpdater = DialogDataUpdater()
    var startDownloadMainPackage: Boolean = false
    var countChangeFragment = 0

    var environmentType: EnvironmentType = EnvironmentType.Master
    var enginesType: EnginesType = EnginesType.New

    var internetStatus:  MutableLiveData<NetworkTypes> = MutableLiveData(NetworkTypes.None)

    var jwtToken:String = ""
    var userName: String = ""
    var paymentId: MutableLiveData<String> = MutableLiveData("")
    var revenuecatInitialize = false
    var uid: String = ""

    const val versionName = BuildConfig.VERSION_NAME
    const val versionCode = BuildConfig.VERSION_CODE

    var premiumVersion: Boolean = false
    var beta: Boolean = false
    var isLogin: MutableLiveData<Boolean> = MutableLiveData(false)
    var isAdmin: MutableLiveData<Boolean> = MutableLiveData(false)

    var showNotifyForTariffIcon: Boolean = true
    var isSessionConfirm: Boolean = false
    var isSessionRestored: Boolean = false
    var isFavouritesSynchronized: Boolean = false
    var isCustomCategorySynchronized: Boolean = false

    var networkType = "WiFi"

    // CONFIG
    var maxVisibleItem: Int = 50
    val maxTariffViewInSignModule = 3

    var downloadFinishId: Long = 0

    // THEME
    var settingFontBoldTariff: MutableLiveData<Boolean> = MutableLiveData(true)
    var settingFontBoldMain: MutableLiveData<Boolean> = MutableLiveData(true)
    var funeralTheme: Boolean = false
    var colorSpan2: Int = 0
    var colorSpan1: Int = 0
    var requireRestart: MutableLiveData<Boolean> = MutableLiveData(false)

    var test = false

    var loadStyle = false
    //. var themeName: Int = R.style.defaultTheme
    // CUSTOM CATEGORY
    var showCustomOnTop: MutableLiveData<Boolean> = MutableLiveData(true)
    var customCategoryMapList = listOf<MapCategory>()
    var customCategoryList = listOf<CustomCategory>()

    val dialogDownloadMessage: MutableLiveData<String> = MutableLiveData("")
    val dialogDownloadFileProgress: MutableLiveData<Int> = MutableLiveData(null)
    var dialogDownloadFileMessage: String = ""
    var setIndeterminate: MutableLiveData<Boolean> = MutableLiveData(false)

}