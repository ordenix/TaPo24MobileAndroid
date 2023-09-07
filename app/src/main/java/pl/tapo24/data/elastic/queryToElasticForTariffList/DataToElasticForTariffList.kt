package pl.tapo24.data.elastic.queryToElasticForTariffList

data class DataToElasticForTariffList(
    val page: Page,
    val query: String
)