package pl.tapo24.ui.road.categoryDrivingLicence

import android.text.SpannableStringBuilder
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
import pl.tapo24.utils.UlListBuilder
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
        return UlListBuilder().getSpannableTextBullet(text)
    }
    fun expand(position: Int) {
        data.value!![position].expand = !data.value!![position].expand
        adapter.notifyDataSetChanged()
    }
}