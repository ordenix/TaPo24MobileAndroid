package pl.tapo24.twa.ui.helpers.pointsCalc.basic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.adapter.PointCalculatorBasicAdapter
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.dbData.entity.CodePointsNew
import javax.inject.Inject

@HiltViewModel
class PointsCalculatorBasicViewModel @Inject constructor(
    private val dataTapoDb: DataTapoDb
): ViewModel() {
    lateinit var adapter: PointCalculatorBasicAdapter

    val itemsCodePoints = MutableLiveData<List<CodePointsNew>>()
    val sumPoints = MutableLiveData<Int>(0)

    val itemsCodePointsCalc = MutableLiveData<List<CodePointsNew>>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val dataFromDb = async { dataTapoDb.codePointsNew().getAll() }.await()
            if (dataFromDb.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    itemsCodePoints.value = dataFromDb
                }
            }
        }

    }

    fun addToList(item: CodePointsNew) {
        var list: MutableList<CodePointsNew> = mutableListOf()
        if (itemsCodePointsCalc.value == null) {
            list.add(item)
        } else {
            list = (itemsCodePointsCalc.value as MutableList<CodePointsNew>?)!!
            list.add(item)


        }
        itemsCodePointsCalc.value = list
        recalculatePoints()


    }

    private fun recalculatePoints() {
        var points = 0
        itemsCodePointsCalc.value?.forEach { element ->
            points += element.points?: 0
        }
        sumPoints.value = points
    }

    fun removeFromList(position: Int) {
        var list: MutableList<CodePointsNew> = (itemsCodePointsCalc.value as MutableList<CodePointsNew>?)!!
        list.removeAt(position)
        itemsCodePointsCalc.value = list
        recalculatePoints()

    }
}