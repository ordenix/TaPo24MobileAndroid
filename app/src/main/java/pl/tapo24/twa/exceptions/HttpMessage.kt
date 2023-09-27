package pl.tapo24.twa.exceptions

enum class HttpMessage(val message: String) {
    PostalCodeNotFound("Nie znaleziono kodu w bazie"),
    PostalCityNotFound("Nie znaleziono miasta w bazie")
}