package pl.tapo24.ui.road.countryDrivingLicence

import android.text.SpannableStringBuilder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.adapter.CountryDrivingLicenceAdapter
import pl.tapo24.dbData.DataTapoDb
import pl.tapo24.dbData.entity.CountryDrivingLicence
import pl.tapo24.utils.UlListBuilder
import javax.inject.Inject

@HiltViewModel

class CountryDrivingLicenceViewModel @Inject constructor(
    private val dataTapoDb: DataTapoDb) : ViewModel() {
    lateinit var adapter: CountryDrivingLicenceAdapter
    val data = MutableLiveData<List<CountryDrivingLicence>>()

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            var dataFromDb: List<CountryDrivingLicence>? = null
            async{dataFromDb = dataTapoDb.countryDivingLicence().getAll()}.await()
            withContext(Dispatchers.Main) {
                if (dataFromDb != null) {
                    data.value = dataFromDb!!
                }
            }
        }

    }
    fun getSpannableText(text: String?): SpannableStringBuilder {
        return UlListBuilder().getTextNumerator(text)
    }

}