package pl.tapo24.twa.data

import androidx.lifecycle.MutableLiveData
import pl.tapo24.twa.BuildConfig
import pl.tapo24.twa.R

object State {
    var startDownloadMainPackage: Boolean = false
    var countChangeFragment = 0

    var environmentType: EnvironmentType = EnvironmentType.Master
    var enginesType: EnginesType = EnginesType.New

    var internetStatus:  MutableLiveData<Int> = MutableLiveData(0)

    var jwtToken:String = ""
    var userName: String = ""
    var uid: String = ""

    const val versionName = BuildConfig.VERSION_NAME
    const val versionCode = BuildConfig.VERSION_CODE

    var premiumVersion: Boolean = false
    var isLogin: MutableLiveData<Boolean> = MutableLiveData(false)
    var isSessionConfirm: Boolean = false
    var isSessionRestored: Boolean = false
    var isFavouritesSynchronized: Boolean = false
    var isCustomCategorySynchronized: Boolean = false

    var networkType = "WiFi"

    // CONFIG
    var maxVisibleItem: Int = 50

    var downloadFinishId: Long = 0

    // THEME
    var settingFont: MutableLiveData<FontTypes> = MutableLiveData(FontTypes.itim)
    var settingTheme : MutableLiveData<ThemeTypes> = MutableLiveData()
    var settingFontBoldTariff : MutableLiveData<Boolean> = MutableLiveData(true)
    var settingFontBoldMain : MutableLiveData<Boolean> = MutableLiveData(true)


    var test = false

    var loadStyle = false
    //. var themeName: Int = R.style.defaultTheme
}