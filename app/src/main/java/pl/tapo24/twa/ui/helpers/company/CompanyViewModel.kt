package pl.tapo24.twa.ui.helpers.company

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.adapter.CompanyAdapter
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.dbData.entity.Company
import pl.tapo24.twa.utils.ColorSpan
import javax.inject.Inject

@HiltViewModel
class CompanyViewModel @Inject constructor(
    private val dataTapoDb: DataTapoDb
): ViewModel(), ColorSpan {
    val data: MutableLiveData<List<Company>> = MutableLiveData()
    lateinit var adapter: CompanyAdapter
    override var searchText: String = ""
    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            val companyData = async { dataTapoDb.company().getAll()  }.await()
            withContext(Dispatchers.Main) {
                data.value = companyData

            }
        }

    }
}