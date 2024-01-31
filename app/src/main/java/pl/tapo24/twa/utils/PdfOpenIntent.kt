package pl.tapo24.twa.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File

class PdfOpenIntent(
    val context: Context
) {
    //    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            val intent = result.data
//            println("RESULT")
//            // Handle the Intent
//        }
//    }
    fun openPDF(fileName: String, isPublicStorage: Boolean) {
        val file: File = if (isPublicStorage) {
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "tapo24Don'tDelete/pdf/${fileName}")

        } else {
            File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "pdf/${fileName}")

        }
        val target = Intent(Intent.ACTION_VIEW)

        val fURI = FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            file
        )
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        target.setDataAndType(fURI, "application/pdf")
        target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or  Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        val intent = Intent.createChooser(target, "Otwórz plik w: (nastąpi przekierowanie do zewnętrznej przeglądarki plików pdf")
        //val intent = Intent(target).setPackage("com.adobe.reader")


        try {
            //startForResult.launch(intent)
            context.startActivity(intent)

        } catch (e: ActivityNotFoundException) {
            // Instruct the user to install a PDF reader here, or something
        }
    }


}