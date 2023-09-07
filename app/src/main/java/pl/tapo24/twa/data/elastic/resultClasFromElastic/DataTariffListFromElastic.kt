package pl.tapo24.twa.data.elastic.resultClasFromElastic

data class DataTariffListFromElastic(
    val meta: Meta,
    val results: List<Result>
)