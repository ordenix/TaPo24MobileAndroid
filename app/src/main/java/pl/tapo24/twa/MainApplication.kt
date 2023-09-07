package pl.tapo24.twa


import android.app.Application
import android.content.Context
import android.widget.Toast
import dagger.hilt.android.HiltAndroidApp
import org.acra.config.httpSender
import org.acra.config.toast
import org.acra.data.StringFormat
import org.acra.ktx.initAcra
import org.acra.sender.HttpSender

@HiltAndroidApp
class MainApplication: Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        initAcra {
            reportFormat = StringFormat.JSON
            httpSender {
                uri = "https://acra.tapo24.pl/report" /*best guess, you may need to adjust this*/
                basicAuthLogin = "LEEPculNAAaeiJgr"
                basicAuthPassword = "7kaqOKxTeEuWP5tm"
                httpMethod = HttpSender.Method.POST
            }
//            dialog {
//                //required
//                text = "Przykro nam, że spotkało to właśnie Ciebie"
//                //optional, enables the dialog title
//                title = "Oj awaria :("
//                //defaults to android.R.string.ok
//                positiveButtonText = "getString(R.string.dialog_positive)"
//                //defaults to android.R.string.cancel
//                //negativeButtonText = "getString(R.string.dialog_negative)"
//                //optional, enables the comment input
//                commentPrompt = "getString(R.string.dialog_comment)"
//                //optional, enables the email input
//                //emailPrompt =" getString(R.string.dialog_email)"
//                //defaults to android.R.drawable.ic_dialog_alert
//                //resIcon = "R.drawable.dialog_icon"
//                //optional, defaults to @android:style/Theme.Dialog
//               // resTheme = "R.style.dialog_theme"
//                //allows other customization
//                //reportDialogClass = MyCustomDialog::class.java
//            }

            toast {
                //required
                text = "Oj awaria :( Przykro nam, że cb to spotkało. Juz został ogłoszony alarm u programisty i pracuje aby usunąć ten błąd"
                //defaults to Toast.LENGTH_LONG
                length = Toast.LENGTH_LONG
            }
        }
    }
}