package pl.tapo24.data

enum class EnginesType(val description: String,
    val url: String) {
    Old("Engine before","https://elastic-enterprise-search.server.tapo24.pl/api/as/v1/engines/tapo24beforeseptember/"),
    New("New engine after changes in september", "https://elastic-enterprise-search.server.tapo24.pl/api/as/v1/engines/tapo24new/")
}