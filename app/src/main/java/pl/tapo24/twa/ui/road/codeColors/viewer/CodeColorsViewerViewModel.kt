package pl.tapo24.twa.ui.road.codeColors.viewer

import android.text.SpannableStringBuilder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.adapter.CodeColorsAdapter
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.dbData.entity.CodeColors
import pl.tapo24.twa.utils.UlListBuilder
import javax.inject.Inject

@HiltViewModel
class CodeColorsViewerViewModel
    @Inject constructor(
        private val dataTapoDb: DataTapoDb
    )
    : ViewModel() {

    val data = MutableLiveData<List<CodeColors>>()
    lateinit var adapter: CodeColorsAdapter

    fun getData(code: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val dataFromDb = async { dataTapoDb.codeColors().getByCode(code) }.await()
            withContext(Dispatchers.Main) {
                data.value = dataFromDb
            }
        }
    }

    fun returnBulletList(text: String): SpannableStringBuilder {
        return UlListBuilder().getSpannableTextBulletFromCustomText(text)
    }

}