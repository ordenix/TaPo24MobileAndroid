package pl.tapo24.twa.data

import androidx.lifecycle.MutableLiveData
import pl.tapo24.twa.BuildConfig

object State {
    var countChangeFragment = 0

    var environmentType: EnvironmentType = EnvironmentType.Master
    var enginesType: EnginesType = EnginesType.New

    var internetStatus:  MutableLiveData<Int> = MutableLiveData(0)

    var jwtToken:String = ""
    var userName: String = ""
    var uid: String = ""

    const val versionName = BuildConfig.VERSION_NAME
    const val versionCode = BuildConfig.VERSION_CODE

    var premiumVersion: Boolean = true
    var isLogin: MutableLiveData<Boolean> = MutableLiveData(false)
    var isSessionConfirm: Boolean = false
    var isSessionRestored: Boolean = false
    var isFavouritesSynchronized: Boolean = false

    var networkType = "WiFi"

    // CONFIG
    var maxVisibleItem: Int = 50



}