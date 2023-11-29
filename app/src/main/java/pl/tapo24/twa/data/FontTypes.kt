package pl.tapo24.twa.data

import pl.tapo24.twa.R

enum class FontTypes(
    val type: String,
    val themeName: Int
) {
    itim("Itim", R.style.itim),
    sansSerifLight("sansSerifLight", R.style.sansSerifLight),
    arimo("arimo", R.style.arimo),
    alegreya("alegreya", R.style.alegreya),
    armata("armata", R.style.armata)
}