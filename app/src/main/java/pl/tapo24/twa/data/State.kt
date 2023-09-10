package pl.tapo24.twa.data

import pl.tapo24.twa.BuildConfig

object State {


    var environmentType: EnvironmentType = EnvironmentType.Master
    var enginesType: EnginesType = EnginesType.New

    var internetStatus: Int = 0

    val jwtToken:String = ""
    val userName: String = ""
    var uid: String = ""

    const val versionName = BuildConfig.VERSION_NAME
    const val versionCode = BuildConfig.VERSION_CODE

    val premiumVersion: Boolean = true
    var isLogin: Boolean = false

    var networkType = "WiFi"

    // CONFIG
    var maxVisibleItem: Int = 50

    var countChangeFragment = 0

}