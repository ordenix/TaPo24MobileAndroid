package pl.tapo24.twa.data.login

enum class Providers(val type: String) {
    Local(type = "local"),
    Google(type = "google")
}
