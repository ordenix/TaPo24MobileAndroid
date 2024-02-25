package pl.tapo24.twa.data

import pl.tapo24.twa.R

enum class FontTypes(
    val type: String,
    val themeName: Int,
    val isNonPremium: Boolean,
    val isHiddenInList: Boolean,
    val namePl: String,
    val rIdFont:Int? = null
) {
    itim("Itim", R.style.itim, true, false, "Itim - Domyślny", R.font.itim),
    sansSerifLight("sansSerifLight", R.style.sansSerifLight,true, false, "sansSerifLight", ),
    arimo("arimo", R.style.arimo,false, false, "Arimo", R.font.arimo),
    alegreya("alegreya", R.style.alegreya,false, false, "Alegreya",R.font.alegreya),
    armata("armata", R.style.armata,false, false, "Armata",R.font.armata),
    patric("patrick_hand", R.style.patrick_hand, false, false, "Patrick_hand", R.font.patrick_hand), //6
    average("average",R.style.average, false, false,"Average", R.font.average),
    scada("scada", R.style.scada, false,false,"Scada", R.font.scada),
    mulish("mulish", R.style.mulish_extralight, false,false, "Mulish_extralight", R.font.mulish_extralight),
    farro("farro", R.style.farro, false, false, "Farro", R.font.farro)

}