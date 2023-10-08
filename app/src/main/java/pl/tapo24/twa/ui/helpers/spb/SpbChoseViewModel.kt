package pl.tapo24.twa.ui.helpers.spb

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.adapter.SpbChoseAdapter
import pl.tapo24.twa.data.SpbChose
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.dbData.entity.Spb
import javax.inject.Inject


@HiltViewModel
class SpbChoseViewModel @Inject constructor(
    private val dataTapoDb: DataTapoDb
) : ViewModel() {
    var isSpbFragment: Boolean = false
    val data: MutableLiveData<List<SpbChose>> = MutableLiveData()
    lateinit var adapter: SpbChoseAdapter

    fun getData() {
        var dataFromDb: List<Spb>? = null
        val spbChoseList: MutableList<SpbChose> = mutableListOf()
        viewModelScope.launch(Dispatchers.IO) {
            if (isSpbFragment) {
                // get SPB
                async {
                    dataFromDb = dataTapoDb.spb().getAllSpb()
                }.await()
            } else {
                // get all list
                val firstElement: SpbChose = SpbChose("Przypadki użycia ŚPB", "hand_point_up_solid")
                spbChoseList.add(firstElement)
                async {
                    dataFromDb = dataTapoDb.spb().getAllNotSpb()
                }.await()

            }

            if (dataFromDb  != null) {
                val convertData = mapSpbToSpbChose(dataFromDb!!)
                spbChoseList.addAll(convertData)
            }
            withContext(Dispatchers.Main) {
                data.value = spbChoseList
            }

        }
    }

    private fun mapSpbToSpbChose(items: List<Spb>): List<SpbChose> {
        val tempList: MutableList<SpbChose> = mutableListOf()

        items.forEach {
            val elementToAdd: SpbChose? = SpbChose(it.type?: "", it.path ?: "", it.path2)
            if (elementToAdd != null) {
                tempList.add(elementToAdd)
            }
        }

        return tempList

    }


}