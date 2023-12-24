package pl.tapo24.twa.data

enum class EnvironmentType(val description: String, val url: String) {
    Beta("Beta", "https://develop.api3.tapo24.pl/api/"),
    Feature("Feature", "http://192.168.0.59:8081/api/"),
    Master("Production", "https://api3.tapo24.pl/api/")

}