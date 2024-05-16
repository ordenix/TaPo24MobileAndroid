package pl.tapo24.twa.ui.utils.navigation

import androidx.core.os.bundleOf
import pl.tapo24.twa.R
import pl.tapo24.twa.data.nav.NavElement

enum class NavHelpers(
    val navElement: NavElement
) {

    PostalCode(
        NavElement(
        navName = "Kody pocztowe",
        navIcon= R.drawable.envelope_solid,
        navId = R.id.action_nav_helpers_to_nav_postalCode,
        navHttpLink = null
    )
    ),
    LinkCheckOc(
        NavElement(
        navName = "Link do sprawdzenia OC w UFG",
        navIcon= R.drawable.car_burst_solid,
        navId = null,
        navHttpLink = "https://www.ufg.pl/infoportal/faces/pages_home-page/Page_4d98135c_14e2b8ace27__7ff1/Pagee0e22f3_14efe6adc05__7ff1/Page4d024e07_14f0a824115__7ff6?_afrLoop=3753003479910681&_afrWindowMode=0&_adf.ctrl-state=182qsvy3xd_29"
    )
    ),
    LinkCheckDrivingLicence(
        NavElement(
        navName = "Link do sprawdzenia uprawnień",
        navIcon= R.drawable.id_card_solid,
        navId = null,
        navHttpLink = "https://moj.gov.pl/uslugi/engine/ng/index?xFormsAppName=UprawnieniaKierowcow&xFormsOrigin=EXTERNAL"
    )
    ),
    LinkCarHistory(
        NavElement(
        navName = "Link do sprawdzenia historii pojazdu",
        navIcon= R.drawable.history,
        navId = null,
        navHttpLink = "https://historiapojazdu.gov.pl/"
    )
    ),
    PdfPatterns(
        NavElement(
        navName = "Wzory protokołów",
        navIcon= R.drawable.clipboard_check_solid,
        navId = R.id.action_nav_helpers_to_nav_law_with_args,
        requireLogin = true,
        navHttpLink = null
    )
    ),
    Prado(
        NavElement(
        navName = "Kontrola autentyczności dokumentów",
        navIcon= R.drawable.clipboard_check_solid,
        navId = R.id.action_nav_helpers_to_nav_validDocument,
        navHttpLink = null
    )
    ),
    TelephoneBook(
        NavElement(
        navName = "Książka telefoniczna",
        navIcon= R.drawable.telephone,
        navId = R.id.action_nav_helpers_to_nav_telephone,
        navHttpLink = null,
        listBundle = bundleOf("bookmark" to null)
    )
    ),
    LinkCheck12t(
        NavElement(
        navName = "Link do ograniczeń ruchu pow. 12t",
        navIcon= R.drawable.block_bold,
        navId = null,
        requireLogin = true,
        navHttpLink = "https://www.gov.pl/web/infrastruktura/ograniczenia-w-ruchu"
    )
    ),
    Spb(
        NavElement(
        navName = "ŚPB",
        navIcon= R.drawable.gun_solid,
        navId = R.id.action_nav_helpers_to_nav_spbChose,
        navHttpLink = null
    )
    ),
//    SpeedCalc(
//        NavElement(
//        navName = "Kalkulator prędkości",
//        navIcon= R.drawable.gauge_high_solid,
//        navId = R.id.action_nav_helpers_to_nav_speedCalc,
//        navHttpLink = null
//    )
//    ),
    Note(
        NavElement(
        navName = "Notatnik",
        navIcon= R.drawable.clipboard_solid,
        navId = R.id.action_nav_helpers_to_nav_note,
        navHttpLink = null
    )
    ),
//    AlcoConverter(
//        NavElement(
//        navName = "Przelicznik mn/l - ‰",
//        navIcon= R.drawable.calculator_solid,
//        navId = R.id.action_nav_helpers_to_nav_alcoholCalc,
//        navHttpLink = null
//    )
//    ),
    BuyCaffe(
        NavElement(
        navName = "Postaw nam kawę",
        navIcon= R.drawable.mug_hot_solid,
        navId = null,
        navHttpLink = "https://buycoffee.to/tapo24",
        statsDescription = "buy caffe"
    )
    ),
    Patronite(
        NavElement(
        navName = "Wesprzyj nas",
        navIcon= R.drawable.patronite_mini_svg_02,
        navId = null,
        navHttpLink = "https://patronite.pl/tapo24",
        statsDescription = "patron"
    )
    ),
    Calculators(
        NavElement(
            navName = "Kalkulatory/ Preliczniki",
            navIcon= R.drawable.calculator_solid,
            navId = R.id.action_nav_helpers_to_nav_innerNavigation,
            navHttpLink = null,
            listBundle = bundleOf("title" to "Kalkulatory", "treeName" to "calculator")
        )
    ),
    AdrTable(
        NavElement(
            navName = "Tablica ADR",
            navIcon= R.drawable.explosion_solid,
            navId = R.id.action_nav_helpers_to_nav_adrTable,
            requirePremium = true,
            navHttpLink = null,
        )
    ),
    Company(
        NavElement(
            navName = "Lista Firm",
            navIcon= R.drawable.company,
            navId = R.id.action_nav_helpers_to_companyFragment,
            requirePremium = true,
            navHttpLink = null,
        )
    ),

}