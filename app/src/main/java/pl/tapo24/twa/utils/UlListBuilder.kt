package pl.tapo24.twa.utils

import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BulletSpan

class UlListBuilder {

    /**
     * Prepare bullet spannable string. Use for slice [/n] [forceFirst].
     *
     * @param forceFirst Define bullet for fist element.

     */
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
    /**
     * Prepare bullet spannable string. Use for slice [/n] [forceFirst].
     *
     * @param forceFirst Define bullet for fist element.

     */
    fun getSpannableTextBullet(list: List<String>, forceFirst: Boolean = false): SpannableStringBuilder {
        val ssb = SpannableStringBuilder()
        if (list.isEmpty()) return ssb
        if (list.size == 1 && !forceFirst) {
            ssb.append(list[0].trimStart().replaceFirstChar{it.uppercase()})
        } else {
            list.forEach{ element ->
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

    fun getTextDash(text: String?, forceFirst: Boolean = false): SpannableStringBuilder {
        val ssb = SpannableStringBuilder()
        if (text == null) return ssb
        val array: List<String> = text.split("/n").toList()
        if (array.size == 1 && !forceFirst) {
            ssb.append(text.trimStart().replaceFirstChar{it.uppercase()})
        } else {
            array.forEach{
                val ss = SpannableString("- ${it.trimStart().replaceFirstChar{it.uppercase()}}")
                //ss.setSpan(BulletSpan(10), 0, it.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                ssb.append(ss)
                ssb.append("\n")

            }
        }
        return ssb
    }



    fun getSpannableTextListWithoutBullet(text: String?, extraSeparateLine: Boolean = false): SpannableStringBuilder {
        val ssb = SpannableStringBuilder()
        if (text == null) return ssb
        val array: List<String> = text.split("/n").toList()
        array.forEach{ element ->
            val textTrim = element.trimStart().replaceFirstChar{it.uppercase()}
            if (!textTrim.contains("Null",true)) {
                ssb.append(textTrim)
                ssb.append("\n")
                if (extraSeparateLine) {
                    ssb.append("\n")
                }
            }
        }

        return ssb

    }


}