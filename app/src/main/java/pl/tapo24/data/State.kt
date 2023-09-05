package pl.tapo24.data

import pl.tapo24.BuildConfig
import pl.tapo24.db.entity.Tariff

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

}