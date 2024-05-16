package pl.tapo24.twa.ui.utils.navigation.inner

import pl.tapo24.twa.R
import pl.tapo24.twa.data.nav.NavElement

enum class NavTechnicalCondition (
    val navElement: NavElement
) {
    Noise(
        NavElement(
            navName = "Poziom hałasu",
            navIcon= R.drawable.noise,
            navId = R.id.action_nav_innerNavigation_to_noiseLevelFragment,
            navHttpLink = null
        )
    ),
}