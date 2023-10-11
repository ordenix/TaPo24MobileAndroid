package pl.tapo24.twa.data.postal

data class ResponseCodeSequenceContent(
    val codeSequence: List<String>,
    val gmina: String,
    val miejscowosc: String,
    val powiat: String,
    val wojewodztwo: String
)