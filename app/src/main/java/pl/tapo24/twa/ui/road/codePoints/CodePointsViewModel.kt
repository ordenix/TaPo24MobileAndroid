package pl.tapo24.twa.ui.road.codePoints

import android.text.SpannableStringBuilder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.adapter.CodePointsAdapter
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.dbData.entity.CodePointsNew
import pl.tapo24.twa.utils.UlListBuilder
import javax.inject.Inject

@HiltViewModel
class CodePointsViewModel @Inject constructor(
    private val dataTapoDb: DataTapoDb
)
    : ViewModel() {
        val data: MutableLiveData<List<CodePointsNew>> = MutableLiveData()
        lateinit var adapter: CodePointsAdapter


        fun getData() {
            viewModelScope.launch(Dispatchers.IO) {
                var dataFromDb: List<CodePointsNew>? = null

                async { dataFromDb = dataTapoDb.codePointsNew().getAll() }.await()

                withContext(Dispatchers.Main) {
                    if (dataFromDb != null) {
                        data.value = dataFromDb!!
                    }
                }

            }
        }

        fun prepareBulletText(text: String?): SpannableStringBuilder {
            return UlListBuilder().getSpannableTextBullet(text, true)
        }


}