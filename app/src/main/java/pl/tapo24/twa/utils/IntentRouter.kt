package pl.tapo24.twa.utils

import android.content.Intent
import android.net.Uri
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import pl.tapo24.twa.R

class IntentRouter {
    fun route(intent: Intent?, navController: NavController?)   {
        val data: Uri? = intent?.data
        if (data != null && data.toString().contains("ActivateAccount")) {
            navController?.navigate(R.id.nav_accountActivate, bundleOf("url" to data.toString()))
        }
    }
}