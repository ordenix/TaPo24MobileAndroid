package pl.tapo24.data.elastic.resultClasFromElastic

data class Page(
    val current: Int,
    val size: Int,
    val total_pages: Int,
    val total_results: Int
)