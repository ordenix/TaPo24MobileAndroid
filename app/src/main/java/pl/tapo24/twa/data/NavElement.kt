package pl.tapo24.twa.data

import android.os.Bundle

data class NavElement(    val navName: String,
                          val navIcon: Int,
                          val navId: Int?,
                          val navHttpLink: String?,
                          val requireLogin: Boolean = false,
                          val requirePremium: Boolean = false,
                            val listBundle: Bundle? = null ) {

}
