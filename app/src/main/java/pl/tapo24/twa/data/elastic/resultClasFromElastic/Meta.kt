package pl.tapo24.twa.data.elastic.resultClasFromElastic

data class Meta(
    val alerts: List<Any>,
    val engine: Engine,
    val page: Page,
    val precision: Int,
    val request_id: String,
    val warnings: List<Any>
)