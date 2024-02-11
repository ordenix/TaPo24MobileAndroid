package pl.tapo24.twa.ui.helpers.telephone

import android.text.SpannableString
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.adapter.TelephoneAdapter
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.dbData.entity.TelephoneNumbers
import pl.tapo24.twa.utils.ColorSpan
import pl.tapo24.twa.utils.SpanFilter
import javax.inject.Inject

@HiltViewModel
class TelephoneViewModel
@Inject constructor(
    private val dataTapoDb: DataTapoDb
) : ViewModel(), ColorSpan {
    lateinit var adapter: TelephoneAdapter
    val selectedNumber: MutableLiveData<String> = MutableLiveData()
    override var searchText: String = ""

    var bookmarkClicked: String? = null
    lateinit var telephoneListAll: List<TelephoneNumbers>
    var telephoneList = MutableLiveData<List<TelephoneNumbers>>()


    fun getTelephoneList() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = async {dataTapoDb.telephoneNumbers().getAll()}.await()
            if (list.isNotEmpty()) {
                    telephoneListAll = list
                withContext(Dispatchers.Main) {
                   filterBookmarks()
                }
            }

        }
    }
//    fun getSpanText(text: String?): SpannableString? {
//        return SpanFilter().getSimpleSpanText(text, searchText, colorSpan1)
//    }
    private fun filterBookmarks() {
        if (bookmarkClicked != null) {
            if (bookmarkClicked == "OTHERS") {
                telephoneList.value = telephoneListAll.filter { !it.isBorderGuard && !it.isRoadInspection }

            } else if (bookmarkClicked == "ITD") {
                telephoneList.value = telephoneListAll.filter { it.isRoadInspection }
            } else {
                // sg
                telephoneList.value = telephoneListAll.filter { it.isBorderGuard }

            }
        } else {
            telephoneList.value = telephoneListAll
        }

    }
    fun onTelephoneClick(telephone: String) {
        selectedNumber.value = telephone
    }


}