package pl.tapo24.twa.data.register

data class RegisterForm(
    val acceptAdv: Boolean,
    val acceptRules: Boolean,
    val email: String,
    val login: String,
    val password: String?,
    val googleToken: String? = null
)