package pl.tapo24.data.elastic.queryToElasticForTariffList

data class Page(
    var current: Int = 1,
    var size: Int= 150
)