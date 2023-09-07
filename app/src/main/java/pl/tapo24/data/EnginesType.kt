package pl.tapo24.data

enum class EnginesType(val description: String,
    val url: String,
    val bearer: String) {
    Old("Engine before","https://elastic-enterprise-search.server.tapo24.pl/api/as/v1/engines/tapo24beforeseptember/","Bearer search-gwuqjnb5u2je9vnc6e7i5gnd"),
    New("New engine after changes in september", "https://elastic-enterprise-search.server.tapo24.pl/api/as/v1/engines/tapo24new/","Bearer search-gwuqjnb5u2je9vnc6e7i5gnd")
}