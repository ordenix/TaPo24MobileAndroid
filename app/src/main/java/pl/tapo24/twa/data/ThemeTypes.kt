package pl.tapo24.twa.data

import pl.tapo24.twa.R

enum class ThemeTypes(
    val type: String,
    val themeName: Int
) {
    default("default", R.style.defaultTheme)
}