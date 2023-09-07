package pl.tapo24.twa.data.elastic.queryToElasticForTariffList

data class DataToElasticForTariffList(
    val page: Page,
    val query: String
)