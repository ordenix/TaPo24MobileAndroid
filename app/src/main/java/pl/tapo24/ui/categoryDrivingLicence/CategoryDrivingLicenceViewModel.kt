package pl.tapo24.ui.categoryDrivingLicence

import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BulletSpan
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.adapter.CategoryDrivingLicenceAdapter
import pl.tapo24.dbData.DataTapoDb
import pl.tapo24.dbData.entity.CodeDrivingLicence
import javax.inject.Inject


@HiltViewModel
class CategoryDrivingLicenceViewModel @Inject constructor(
    private val dataTapoDb: DataTapoDb,

): ViewModel()  {
    lateinit var adapter: CategoryDrivingLicenceAdapter
    val data = MutableLiveData<List<CodeDrivingLicence>>()

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            var dataFromDb: List<CodeDrivingLicence>? = null
            async{ dataFromDb = dataTapoDb.codeDrivingLicence().getAll()} .await()
            withContext(Dispatchers.Main) {
                if (dataFromDb != null) {
                    data.value = dataFromDb!!
                }
            }

        }

    }

    fun getSpannableText(text: String?): SpannableStringBuilder {
        val ssb = SpannableStringBuilder()
        if (text == null) return ssb
        val array: List<String> = text.split("/n").toList()

        array.forEach{
            val ss = SpannableString(it)
            ss.setSpan(BulletSpan(10), 0, it.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            ssb.append(ss)

            //avoid last "\n"

            //avoid last "\n"
             ssb.append("\n")
        }
        return ssb

    }
    fun expand(position: Int) {
        data.value!![position].expand = !data.value!![position].expand
        adapter.notifyDataSetChanged()
    }
}