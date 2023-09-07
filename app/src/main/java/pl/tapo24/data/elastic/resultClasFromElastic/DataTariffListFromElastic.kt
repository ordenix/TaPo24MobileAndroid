package pl.tapo24.data.elastic.resultClasFromElastic

data class DataTariffListFromElastic(
    val meta: Meta,
    val results: List<Result>
)