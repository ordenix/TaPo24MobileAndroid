package pl.tapo24.twa.ui.road.codeLimitsDrivingLicence

import android.text.SpannableString
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.adapter.CodeLimitsDrivingLicenceAdapter
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.dbData.entity.CodeLimitsDrivingLicence
import pl.tapo24.twa.utils.ColorSpan
import pl.tapo24.twa.utils.SpanFilter
import javax.inject.Inject

@HiltViewModel
class CodeLimitsDrivingLicenceViewModel @Inject constructor(
    private val dataTapoDb: DataTapoDb
): ViewModel(), ColorSpan {
    override var searchText: String = ""
    val data = MutableLiveData<List<CodeLimitsDrivingLicence>>()
    lateinit var adapter: CodeLimitsDrivingLicenceAdapter

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            var dataFromDb: List<CodeLimitsDrivingLicence>? = null
            async{dataFromDb = dataTapoDb.codeLimitsDrivingLicence().getAll()}.await()
            withContext(Dispatchers.Main) {
                if (dataFromDb != null) {
                    data.value = dataFromDb!!
                }
            }
        }

    }

}