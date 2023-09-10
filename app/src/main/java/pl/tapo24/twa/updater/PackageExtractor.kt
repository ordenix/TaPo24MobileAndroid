package pl.tapo24.twa.updater

import android.os.Environment
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.zip.ZipFile

object PackageExtractor {


    fun unzip( path: String, file: File){
        File(path).run {
            if (!exists()) {
                mkdirs()
            }
        }

        ZipFile(file).use { zip ->

            zip.entries().asSequence().forEach { zipEntry ->
                zip.getInputStream(zipEntry).use { input ->

                    val filePath = path+ File.separator+zipEntry.name

                    if(!zipEntry.isDirectory) {
                       extract(input, filePath)
                    } else {
                        val dir = File(filePath)
                        dir.mkdir()
                    }

                }
            }
        }






    }

    private fun extract(inputStream: InputStream, path: String) {
        val bos = BufferedOutputStream(FileOutputStream(path))
        val bytesIn = ByteArray(4096)
        var read: Int
        while (inputStream.read(bytesIn).also { read = it } != -1) {
            bos.write(bytesIn,0, read)
        }
        bos.close()
    }

}