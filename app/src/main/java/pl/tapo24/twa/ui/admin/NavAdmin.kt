package pl.tapo24.twa.ui.admin

import androidx.core.os.bundleOf
import pl.tapo24.twa.R
import pl.tapo24.twa.data.nav.NavElement

enum class NavAdmin (
    val navElement: NavElement
) {

    PostalCode(
        NavElement(
            navName = "Ankiety",
            navIcon= R.drawable.list_check_solid,
            navId = R.id.action_nav_admin_to_surveyListFragment,
            navHttpLink = null
        )
    )

}