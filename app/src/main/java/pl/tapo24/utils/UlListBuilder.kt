package pl.tapo24.utils

import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BulletSpan
import java.util.*

class UlListBuilder {
    fun getSpannableTextBullet(text: String?, forceFirst: Boolean = false): SpannableStringBuilder {
        val ssb = SpannableStringBuilder()
        if (text == null) return ssb
        val array: List<String> = text.split("/n").toList()
        if (array.size == 1 && !forceFirst) {
            ssb.append(text.trimStart().replaceFirstChar{it.uppercase()})
        } else {
            array.forEach{ element ->
                val textTrim = element.trimStart().replaceFirstChar{it.uppercase()}
                val ss = SpannableString(textTrim)
                ss.setSpan(BulletSpan(10), 0, textTrim.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                ssb.append(ss)
                ssb.append("\n")
            }
        }
        return ssb

    }

    fun getTextNumerator(text: String?, forceFirst: Boolean = false): SpannableStringBuilder {
        val ssb = SpannableStringBuilder()
        if (text == null) return ssb
        val array: List<String> = text.split("/n").toList()
        if (array.size == 1 && !forceFirst) {
            ssb.append(text.trimStart().replaceFirstChar{it.uppercase()})
        } else {
            var numerator: Int = 1
            array.forEach{
                val ss = SpannableString("${numerator}). ${it.trimStart().replaceFirstChar{it.uppercase()}}")
                //ss.setSpan(BulletSpan(10), 0, it.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                ssb.append(ss)
                ssb.append("\n")
                numerator += 1
            }
        }
        return ssb
    }
}