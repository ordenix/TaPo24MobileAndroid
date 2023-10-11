package pl.tapo24.twa.data.postal

data class ResponseCityContent(
    val gmina: String,
    val id: Int,
    val kod: String,
    val miejscowosc: String,
    val nazwa: Any?,
    val numer: Any?,
    val powiat: String,
    val ulica: String,
    val wojewodztwo: String
)