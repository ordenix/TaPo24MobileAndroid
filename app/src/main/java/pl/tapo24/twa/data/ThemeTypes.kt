package pl.tapo24.twa.data

import pl.tapo24.twa.R

enum class ThemeTypes(
    val type: String,
    val themeName: Int,
    val isNonPremium: Boolean,
    val isHiddenInList: Boolean,
    val namePl: String
) {
    Default("default", R.style.defaultTheme, true, false,"Domyślny"),
    GrayTheme("grayTheme", R.style.grayTheme, true, false, "Betonowy sen"),
    BlueMintTheme("blueMintTheme", R.style.blueMintTheme, false, false, "Morska bryza"),
    PinkTheme("pinkTheme", R.style.pinkTheme, false, false,"Pastelowy róż"),
    BlueIceTheme("blueIceTheme", R.style.blueIceTheme, false, false,"Mroźny lód"),
    GreenMintTheme("greenMintTheme", R.style.greenMintTheme, false, false,"Mięta o poranku"),
    DesertTheme("desertTheme", R.style.desertTheme, false, false,"Pustynna burza"),
    FuneralTheme("FuneralTheme", R.style.funeralTheme, false, true,"Pogrzebowa czerń"),
    NTheme("nTheme", R.style.nTheme, false, false,"Krwista czerń"),
    DamianTheme("damianTheme", R.style.damianTheme, false, false,"Mistrzowska szarość"),
    D2Theme("d2Theme", R.style.d2Theme, false, false,"Wiatr na pustyni"),
    G1Theme("g1Theme", R.style.g1Theme, false, false,"Neonowy rozbójnik"),
    G2Theme("g2Theme", R.style.g2Theme, false,  false,"Słoneczny pirat")
}