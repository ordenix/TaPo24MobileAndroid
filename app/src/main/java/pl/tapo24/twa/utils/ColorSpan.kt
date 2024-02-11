package pl.tapo24.twa.utils

import android.text.SpannableString
import pl.tapo24.twa.data.State

interface ColorSpan {


    var searchText: String


    fun getSpanText(text: String?): SpannableString? {
        return SpanFilter().getSimpleSpanText(text, searchText, State.colorSpan1)
    }

    fun getSpanText2(text: String?): SpannableString? {
        return SpanFilter().getSimpleSpanText(text, searchText, State.colorSpan2)
    }
}