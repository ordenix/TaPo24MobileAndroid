package pl.tapo24.utils

import android.graphics.Color
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import pl.tapo24.data.State

class SpanFilter {

    fun getSimpleSpanText(text: String?, searchText: String): SpannableString? {
            // VIP
            if (text == null) return null
            val str = SpannableString(text)
            return if (State.premiumVersion && searchText.isNotEmpty()) {
                val start = text.indexOf(searchText)
                val end = start + searchText.length
                if (start == -1) return str
                str.setSpan(BackgroundColorSpan(Color.rgb(238, 108, 77)), start, end, 0)
                str

            } else {
                str
            }
    }
}