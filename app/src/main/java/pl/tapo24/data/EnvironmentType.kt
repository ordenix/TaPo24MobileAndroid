package pl.tapo24.data

enum class EnvironmentType(val description: String, val url: String) {
    Beta("Beta", "https://develop.api3.tapo24.pl/api/"),
    Feature("Feature", ""),
    Master("Production", "https://api3.tapo24.pl/api/")

}