package pl.tapo24.data

import pl.tapo24.BuildConfig

object State {
    var uid: String = ""
    var isLogin: Boolean = false
    var environmentType: EnvironmentType = EnvironmentType.Master
    var enginesType: EnginesType = EnginesType.New
    val jwtToken:String = ""
    val userName: String = ""
    val versionName = BuildConfig.VERSION_NAME
    val versionCode = BuildConfig.VERSION_CODE
}