package pl.tapo24.twa.data.nav
import androidx.core.os.bundleOf
import pl.tapo24.twa.R
enum class NavRoad(
    val navElement: NavElement
) {
    Sign(NavElement("Znaki drogowe", R.drawable.signs_post_solid, R.id.action_nav_road_to_nav_sign, null)),

    Accident(
        NavElement(
        navName = "Kwalifikacja zdarzenia",
        navIcon= R.drawable.car_burst_solid,
        navId = R.id.action_nav_road_to_nav_story,
        navHttpLink = null,
        listBundle = bundleOf("name" to "Kwalifikacja zdarzenia","storyType" to "accident"))
    ),

    Alcohol(
        NavElement(
        navName = "Kontrola Trzeźwości",
        navIcon= R.drawable.wine_glass_solid,
        navId = R.id.action_nav_road_to_nav_alcohol,
        navHttpLink = null,
        )
    ),
    ControlList(
        NavElement(
        navName = "Kody usterek",
        navIcon= R.drawable.triangle_exclamation_solid,
        navId = R.id.action_nav_road_to_nav_controlList,
        navHttpLink = null,
    )
    ),
    CategoryDrivingLicence(
        NavElement(
        navName = "Kategorie prawa jazdy",
        navIcon= R.drawable.id_card_solid,
        navId = R.id.action_nav_road_to_nav_category_driving_licence,
        navHttpLink = null,
    )
    ),
    CodeDrivinngLicence(
        NavElement(
        navName = "Kody ograniczeń prawa jazdy",
        navIcon= R.drawable.glasses_solid,
        navId = R.id.action_nav_road_to_nav_codeLimitsDrivingLicence,
        navHttpLink = null,
    )
    ),
    OtherCountyDrivingLicence(
        NavElement(
        navName = "Honorowane zagraniczne PJ",
        navIcon= R.drawable.flag_solid,
        navId = R.id.action_nav_road_to_nav_country_driving_licence,
        navHttpLink = null,
    )
    ),
    DocumentsHolding(
        NavElement(
        navName = "Zatrzymywanie dokumentów",
        navIcon= R.drawable.hand_holding_solid,
        navId = R.id.action_nav_road_to_nav_holdingDocumentsChose,
        navHttpLink = null,
    )
    ),
    Status(
        NavElement(
        navName = "Statusy Prawa Jazdy KSIP",
        navIcon= R.drawable.info_solid,
        navId = R.id.action_nav_road_to_nav_status,
        navHttpLink = null,
        requireLogin = true
    )
    ),
    Lighting(
        NavElement(
        navName = "Oświetlenie pojazdu",
        navIcon= R.drawable.lightbulb_solid,
        navId = R.id.action_nav_road_to_nav_lights,
        navHttpLink = null,
    )
    ),
    Towing(
        NavElement(
        navName = "Holowanie pojazdu",
        navIcon= R.drawable.truck_ramp_box_solid,
        navId = R.id.action_nav_road_to_nav_towing,
        navHttpLink = null,
    )
    ),
    Uto(
        NavElement(
        navName = "UTO",
        navIcon= R.drawable.skateboarding_bold,
        navId = R.id.action_nav_road_to_nav_uto,
        navHttpLink = null,
    )
    ),
    PointsCode(
        NavElement(
        navName = "Kody czynów",
        navIcon= R.drawable.book_solid,
        navId = R.id.action_nav_road_to_nav_codePoints,
        navHttpLink = null,
    )
    ),
    Recidivism(
        NavElement(
        navName = "Recydywa",
        navIcon= R.drawable.repeat_solid,
        navId = R.id.action_nav_road_to_nav_recidivism,
        navHttpLink = null,
    )
    ),
    CheckList(
        NavElement(
        navName = "Check listy",
        navIcon= R.drawable.clipboard_check_solid,
        navId = R.id.action_nav_road_to_nav_checkListList,
        requirePremium = true,
        navHttpLink = null,
    )
    ),
    CodeColors(
        NavElement(
            navName = "Kody sprawdzeń KSIP",
            navIcon= R.drawable._123,
            navId = R.id.action_nav_road_to_nav_codeColors,
            requireLogin = true,
            navHttpLink = null,
        )
    ),

}