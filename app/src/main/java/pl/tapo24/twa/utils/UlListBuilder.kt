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

    /**
     * Generate a numbered list using the given text.
     *
     * @param text The input text.
     * @param forceFirst If true, the first line will always be styled by a numerator 1, regardless of the number of lines.
     * @return A SpannableStringBuilder object representing the numbered list.
     */
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

    /**
     * Gets a styled text with dashes for each line in the input text.
     *
     * @param text The input text to style. Can be null.
     * @param forceFirst If true, the first line will always be styled with a dash, regardless of the number of lines.
     * @return A SpannableStringBuilder object containing the styled text.
     */
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



    /**
     * Generates a SpannableStringBuilder object representing a list of text items without bullets.
     *
     * @param text The input text. Can be null.
     * @param extraSeparateLine If true, adds an extra separate line between items.
     * @return A SpannableStringBuilder object representing the list of text items without bullets.
     */
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

    /**
     * Generates a SpannableStringBuilder object representing a list of text items with bullets.
     *
     * @param text The input text. Can be null.
     * @return A SpannableStringBuilder object representing the list of text items with bullets.
     */
    fun getSpannableTextBulletFromCustomText(text: String?): SpannableStringBuilder {
        val ssb = SpannableStringBuilder()
        if (text == null) return ssb
        val list = text.split("/n").toList()
        list.forEach{ element ->
            val textTrim = element.trimStart().replaceFirstChar{it.uppercase()}.trim()
            val ss = SpannableString(textTrim)
            if (textTrim.last() != ':') {
                ss.setSpan(BulletSpan(10), 0, textTrim.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            ssb.append(ss)
            ssb.append("\n")
        }



        return ssb

    }


}