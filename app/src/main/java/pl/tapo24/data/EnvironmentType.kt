package pl.tapo24.data

enum class EnvironmentType(val description: String, val url: String) {
    Beta("Beta", ""),
    Feature("Feature", ""),
    Master("Production", "")

}