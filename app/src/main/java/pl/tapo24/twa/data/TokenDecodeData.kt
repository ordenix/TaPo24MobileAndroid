package pl.tapo24.twa.data

import kotlinx.serialization.Serializable

@Serializable
data class TokenDecodeData (
    val sub: String? = null,
    val uid: String? = null,
    val dbid: Int? = null,
    val role: String? = null,
    val provider: String? = null,
    val iat : Int? =  null,
    val exp: Int? = null
)