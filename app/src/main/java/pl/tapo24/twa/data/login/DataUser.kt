package pl.tapo24.twa.data.login

data class DataUser(
    var login: String? = null,
    var uid: String? = null,
    var dbid: Int? = null,
    var role: String? = null,
    var isBetaTester: Boolean? = null,
    var providers: Providers? = null
)
