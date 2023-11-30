package pl.tapo24.twa.data

import pl.tapo24.twa.R

enum class ThemeTypes(
    val type: String,
    val themeName: Int
) {
    Default("default", R.style.defaultTheme),
    GrayTheme("grayTheme", R.style.grayTheme),
    BlueMintTheme("blueMintTheme", R.style.blueMintTheme),
    PinkTheme("pinkTheme", R.style.pinkTheme),
    BlueIceTheme("blueIceTheme", R.style.blueIceTheme),
    GreenMintTheme("greenMintTheme", R.style.greenMintTheme),
    DesertTheme("desertTheme", R.style.desertTheme)
}