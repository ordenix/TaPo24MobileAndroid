package pl.tapo24.twa.ui.utils.navigation.inner

import pl.tapo24.twa.R
import pl.tapo24.twa.data.nav.NavElement

enum class NavCalc(
    val navElement: NavElement
) {
    SpeedCalc(
        NavElement(
            navName = "Kalkulator prędkości",
            navIcon= R.drawable.gauge_high_solid,
            navId = R.id.action_nav_innerNavigation_to_nav_speedCalc,
            navHttpLink = null
        )
    ),
    AlcoholCalc(
        NavElement(
            navName = "Przelicznik mn/l - ‰",
            navIcon= R.drawable.calculator_solid,
            navId = R.id.action_nav_innerNavigation_to_nav_alcoholCalc,
            navHttpLink = null
        )
    ),
    PointsBasicCalc(
        NavElement(
            navName = "Kalkulator punktów (podstawowy)",
            navIcon= R.drawable.person_military_pointing_solid,
            navId = R.id.action_nav_innerNavigation_to_nav_pointsCalculatorBasic,
            requirePremium = true,
            navHttpLink = null
        )
    ),
}