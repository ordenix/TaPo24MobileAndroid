package pl.tapo24.twa.data.postal

data class ResponseCityWithStreetNumber(

    var id: Long? = null,
    var gmina: String? = null,
    var kod: String? = null,
    var miejscowosc: String? = null,
    var powiat: String? = null,
    var ulica: String? = null,
    var wojewodztwo: String? = null,
    var doField: String? = null,
    var od: String? = null,
    var parzystosc: String? = null,
    
)
