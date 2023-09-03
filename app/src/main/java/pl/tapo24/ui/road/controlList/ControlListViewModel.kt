package pl.tapo24.ui.road.controlList

import android.app.Dialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.adapter.ControlListAdapter
import pl.tapo24.data.PriorityList
import pl.tapo24.data.Standard
import pl.tapo24.dbData.DataTapoDb
import pl.tapo24.dbData.entity.ControlList
import javax.inject.Inject

@HiltViewModel
class ControlListViewModel @Inject constructor(
    private val dataTapoDb: DataTapoDb
): ViewModel() {
    val data = MutableLiveData<List<ControlList>>()
    lateinit var adapter:ControlListAdapter
    val standardList = MutableLiveData<List<Standard>>()
    val priorityList = PriorityList()
    var codeToInsert = ""
    val showDialog = MutableLiveData<Boolean>(false)
    val emptyResults = MutableLiveData<Boolean>(false)

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            var dataFromDb: List<ControlList>? = null
            async{dataFromDb = dataTapoDb.controlList().getAll()}.await()
            withContext(Dispatchers.Main) {
                if (dataFromDb != null) {
                    data.value = dataFromDb!!
                }
            }
        }

    }
    fun removeStandard(code: String) {
        val list = standardList.value as MutableList<Standard>
        val element = list.find { el-> el.code == code }
        if (list.size > 1) {
            list.remove(element)
            standardList.value = list
        } else standardList.value = emptyList()

    }
    fun addStandard(code: String?, level: Int) {
        standardList.value =
            (standardList.value?.plus(code?.let { Standard(it,level) }) ?: listOf(code?.let { Standard(it,level) })) as List<Standard>?

    }
    fun standardClicked(standard: ControlList, code: String) {
        // if you review thats code I know that it isnt optimal
        // but data for this module is specified storaged and my friend who is responsible for actualization this data He only can use simle table
        // data comes form this table page 10 https://isap.sejm.gov.pl/isap.nsf/download.xsp/WDU20190002141/O/D20192141.pdf
        if (code == "a" || code == "") {
            if (data.value?.get(standard.id.toInt())?.standardAddA == true) {
                // curently add
                removeStandard(data.value?.get(standard.id.toInt())?.positionCode + code)
                data.value?.get(standard.id.toInt())?.standardAddA = false
                adapter.notifyDataSetChanged()

            } else {
                // to add
                if (data.value?.get(standard.id.toInt())?.standardLevelFaultsA2 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsA3 == null) {
                    // only one falout A1
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode+ code, 1)

                } else if (data.value?.get(standard.id.toInt())?.standardLevelFaultsA1 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsA3 == null){
                    // only A2
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode+ code, 2)
                } else if (data.value?.get(standard.id.toInt())?.standardLevelFaultsA1 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsA2 == null){
                    // only A3
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode + code, 3)

                } else {
                    // more than 1
                    priorityList.p1 = data.value?.get(standard.id.toInt())?.standardLevelFaultsA1 != null
                    priorityList.p2 = data.value?.get(standard.id.toInt())?.standardLevelFaultsA2 != null
                    priorityList.p3 = data.value?.get(standard.id.toInt())?.standardLevelFaultsA3 != null
                    codeToInsert = data.value?.get(standard.id.toInt())?.positionCode + code
                    showDialog.value = true
                }
                data.value?.get(standard.id.toInt())?.standardAddA = true
                adapter.notifyDataSetChanged()

            }
        }
        ///// b
        if (code == "b") {
            if (data.value?.get(standard.id.toInt())?.standardAddB == true) {
                // curently add
                removeStandard(data.value?.get(standard.id.toInt())?.positionCode + code)
                data.value?.get(standard.id.toInt())?.standardAddB = false
                adapter.notifyDataSetChanged()

            } else {
                // to add
                if (data.value?.get(standard.id.toInt())?.standardLevelFaultsB2 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsB3 == null) {
                    // only one falout B1
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode+ code, 1)

                } else if (data.value?.get(standard.id.toInt())?.standardLevelFaultsB1 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsB3 == null){
                    // only B2
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode+ code, 2)
                } else if (data.value?.get(standard.id.toInt())?.standardLevelFaultsB1 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsB2 == null){
                    // only B3
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode + code, 3)

                } else {
                    // more than 1
                    priorityList.p1 = data.value?.get(standard.id.toInt())?.standardLevelFaultsB1 != null
                    priorityList.p2 = data.value?.get(standard.id.toInt())?.standardLevelFaultsB2 != null
                    priorityList.p3 = data.value?.get(standard.id.toInt())?.standardLevelFaultsB3 != null
                    codeToInsert = data.value?.get(standard.id.toInt())?.positionCode + code
                    showDialog.value = true
                }
                data.value?.get(standard.id.toInt())?.standardAddB = true
                adapter.notifyDataSetChanged()

            }
        }
        ///// C
        if (code == "c") {
            if (data.value?.get(standard.id.toInt())?.standardAddC == true) {
                // curently add
                removeStandard(data.value?.get(standard.id.toInt())?.positionCode + code)
                data.value?.get(standard.id.toInt())?.standardAddC = false
                adapter.notifyDataSetChanged()

            } else {
                // to add
                if (data.value?.get(standard.id.toInt())?.standardLevelFaultsC2 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsC3 == null) {
                    // only one falout C1
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode+ code, 1)

                } else if (data.value?.get(standard.id.toInt())?.standardLevelFaultsC1 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsC3 == null){
                    // only C2
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode+ code, 2)
                } else if (data.value?.get(standard.id.toInt())?.standardLevelFaultsC1 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsC2 == null){
                    // only C3
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode + code, 3)

                } else {
                    // more than 1
                    priorityList.p1 = data.value?.get(standard.id.toInt())?.standardLevelFaultsC1 != null
                    priorityList.p2 = data.value?.get(standard.id.toInt())?.standardLevelFaultsC2 != null
                    priorityList.p3 = data.value?.get(standard.id.toInt())?.standardLevelFaultsC3 != null
                    codeToInsert = data.value?.get(standard.id.toInt())?.positionCode + code
                    showDialog.value = true
                }
                data.value?.get(standard.id.toInt())?.standardAddC = true
                adapter.notifyDataSetChanged()

            }
        }
        ///// d
        if (code == "d") {
            if (data.value?.get(standard.id.toInt())?.standardAddD == true) {
                // curently add
                removeStandard(data.value?.get(standard.id.toInt())?.positionCode + code)
                data.value?.get(standard.id.toInt())?.standardAddD = false
                adapter.notifyDataSetChanged()

            } else {
                // to add
                if (data.value?.get(standard.id.toInt())?.standardLevelFaultsD2 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsD3 == null) {
                    // only one falout D1
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode+ code, 1)

                } else if (data.value?.get(standard.id.toInt())?.standardLevelFaultsD1 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsD3 == null){
                    // only D2
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode+ code, 2)
                } else if (data.value?.get(standard.id.toInt())?.standardLevelFaultsD1 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsD2 == null){
                    // only D3
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode + code, 3)

                } else {
                    // more than 1
                    priorityList.p1 = data.value?.get(standard.id.toInt())?.standardLevelFaultsD1 != null
                    priorityList.p2 = data.value?.get(standard.id.toInt())?.standardLevelFaultsD2 != null
                    priorityList.p3 = data.value?.get(standard.id.toInt())?.standardLevelFaultsD3 != null
                    codeToInsert = data.value?.get(standard.id.toInt())?.positionCode + code
                    showDialog.value = true
                }
                data.value?.get(standard.id.toInt())?.standardAddD = true
                adapter.notifyDataSetChanged()

            }
        }
        ///// e
        if (code == "e") {
            if (data.value?.get(standard.id.toInt())?.standardAddE == true) {
                // curently add
                removeStandard(data.value?.get(standard.id.toInt())?.positionCode + code)
                data.value?.get(standard.id.toInt())?.standardAddE = false
                adapter.notifyDataSetChanged()

            } else {
                // to add
                if (data.value?.get(standard.id.toInt())?.standardLevelFaultsE2 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsE3 == null) {
                    // only one falout E1
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode+ code, 1)

                } else if (data.value?.get(standard.id.toInt())?.standardLevelFaultsE1 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsE3 == null){
                    // only E2
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode+ code, 2)
                } else if (data.value?.get(standard.id.toInt())?.standardLevelFaultsE1 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsE2 == null){
                    // only E3
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode + code, 3)

                } else {
                    // more than 1
                    priorityList.p1 = data.value?.get(standard.id.toInt())?.standardLevelFaultsE1 != null
                    priorityList.p2 = data.value?.get(standard.id.toInt())?.standardLevelFaultsE2 != null
                    priorityList.p3 = data.value?.get(standard.id.toInt())?.standardLevelFaultsE3 != null
                    codeToInsert = data.value?.get(standard.id.toInt())?.positionCode + code
                    showDialog.value = true
                }
                data.value?.get(standard.id.toInt())?.standardAddE = true
                adapter.notifyDataSetChanged()

            }
        }
        ///// f
        if (code == "f") {
            if (data.value?.get(standard.id.toInt())?.standardAddF == true) {
                // curently add
                removeStandard(data.value?.get(standard.id.toInt())?.positionCode + code)
                data.value?.get(standard.id.toInt())?.standardAddF = false
                adapter.notifyDataSetChanged()

            } else {
                // to add
                if (data.value?.get(standard.id.toInt())?.standardLevelFaultsF2 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsF3 == null) {
                    // only one falout F1
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode+ code, 1)

                } else if (data.value?.get(standard.id.toInt())?.standardLevelFaultsF1 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsF3 == null){
                    // only F2
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode+ code, 2)
                } else if (data.value?.get(standard.id.toInt())?.standardLevelFaultsF1 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsF2 == null){
                    // only F3
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode + code, 3)

                } else {
                    // more than 1
                    priorityList.p1 = data.value?.get(standard.id.toInt())?.standardLevelFaultsF1 != null
                    priorityList.p2 = data.value?.get(standard.id.toInt())?.standardLevelFaultsF2 != null
                    priorityList.p3 = data.value?.get(standard.id.toInt())?.standardLevelFaultsF3 != null
                    codeToInsert = data.value?.get(standard.id.toInt())?.positionCode + code
                    showDialog.value = true
                }
                data.value?.get(standard.id.toInt())?.standardAddF = true
                adapter.notifyDataSetChanged()

            }
        }
        ///// g
        if (code == "g") {
            if (data.value?.get(standard.id.toInt())?.standardAddG == true) {
                // curently add
                removeStandard(data.value?.get(standard.id.toInt())?.positionCode + code)
                data.value?.get(standard.id.toInt())?.standardAddG = false
                adapter.notifyDataSetChanged()

            } else {
                // to add
                if (data.value?.get(standard.id.toInt())?.standardLevelFaultsG2 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsG3 == null) {
                    // only one falout G1
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode+ code, 1)

                } else if (data.value?.get(standard.id.toInt())?.standardLevelFaultsG1 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsG3 == null){
                    // only G2
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode+ code, 2)
                } else if (data.value?.get(standard.id.toInt())?.standardLevelFaultsG1 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsG2 == null){
                    // only G3
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode + code, 3)

                } else {
                    // more than 1
                    priorityList.p1 = data.value?.get(standard.id.toInt())?.standardLevelFaultsG1 != null
                    priorityList.p2 = data.value?.get(standard.id.toInt())?.standardLevelFaultsG2 != null
                    priorityList.p3 = data.value?.get(standard.id.toInt())?.standardLevelFaultsG3 != null
                    codeToInsert = data.value?.get(standard.id.toInt())?.positionCode + code
                    showDialog.value = true
                }
                data.value?.get(standard.id.toInt())?.standardAddG = true
                adapter.notifyDataSetChanged()

            }
        }
        ///// h
        if (code == "h") {
            if (data.value?.get(standard.id.toInt())?.standardAddH == true) {
                // curently add
                removeStandard(data.value?.get(standard.id.toInt())?.positionCode + code)
                data.value?.get(standard.id.toInt())?.standardAddH = false
                adapter.notifyDataSetChanged()

            } else {
                // to add
                if (data.value?.get(standard.id.toInt())?.standardLevelFaultsH2 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsH3 == null) {
                    // only one falout H1
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode+ code, 1)

                } else if (data.value?.get(standard.id.toInt())?.standardLevelFaultsH1 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsH3 == null){
                    // only H2
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode+ code, 2)
                } else if (data.value?.get(standard.id.toInt())?.standardLevelFaultsH1 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsH2 == null){
                    // only H3
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode + code, 3)

                } else {
                    // more than 1
                    priorityList.p1 = data.value?.get(standard.id.toInt())?.standardLevelFaultsH1 != null
                    priorityList.p2 = data.value?.get(standard.id.toInt())?.standardLevelFaultsH2 != null
                    priorityList.p3 = data.value?.get(standard.id.toInt())?.standardLevelFaultsH3 != null
                    codeToInsert = data.value?.get(standard.id.toInt())?.positionCode + code
                    showDialog.value = true
                }
                data.value?.get(standard.id.toInt())?.standardAddH = true
                adapter.notifyDataSetChanged()

            }
        }
        ///// i
        if (code == "i") {
            if (data.value?.get(standard.id.toInt())?.standardAddI == true) {
                // curently add
                removeStandard(data.value?.get(standard.id.toInt())?.positionCode + code)
                data.value?.get(standard.id.toInt())?.standardAddI = false
                adapter.notifyDataSetChanged()

            } else {
                // to add
                if (data.value?.get(standard.id.toInt())?.standardLevelFaultsI2 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsI3 == null) {
                    // only one falout I1
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode+ code, 1)

                } else if (data.value?.get(standard.id.toInt())?.standardLevelFaultsI1 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsI3 == null){
                    // only I2
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode+ code, 2)
                } else if (data.value?.get(standard.id.toInt())?.standardLevelFaultsI1 == null &&
                    data.value?.get(standard.id.toInt())?.standardLevelFaultsI2 == null){
                    // only I3
                    addStandard(data.value?.get(standard.id.toInt())?.positionCode + code, 3)

                } else {
                    // more than 1
                    priorityList.p1 = data.value?.get(standard.id.toInt())?.standardLevelFaultsI1 != null
                    priorityList.p2 = data.value?.get(standard.id.toInt())?.standardLevelFaultsI2 != null
                    priorityList.p3 = data.value?.get(standard.id.toInt())?.standardLevelFaultsI3 != null
                    codeToInsert = data.value?.get(standard.id.toInt())?.positionCode + code
                    showDialog.value = true
                }
                data.value?.get(standard.id.toInt())?.standardAddI = true
                adapter.notifyDataSetChanged()

            }
        }

    }

    fun isVisible(point: String, item: ControlList): Boolean {
        var isVisible = false
        when (point) {
            "B" -> {
                if (item.standardLevelFaultsB1 != null || item.standardLevelFaultsB2 != null || (item.standardLevelFaultsB3 != null)) {
                    isVisible = true
                }
            }
            "C" -> {
                if (item.standardLevelFaultsC1 != null || item.standardLevelFaultsC2 != null || (item.standardLevelFaultsC3 != null)) {
                    isVisible = true
                }
            }
            "D" -> {
                if (item.standardLevelFaultsD1 != null || item.standardLevelFaultsD2 != null || (item.standardLevelFaultsD3 != null)) {
                    isVisible = true
                }
            }
            "E" -> {
                if (item.standardLevelFaultsE1 != null || item.standardLevelFaultsE2 != null || (item.standardLevelFaultsE3 != null)) {
                    isVisible = true
                }
            }
            "F" -> {
                if (item.standardLevelFaultsF1 != null || item.standardLevelFaultsF2 != null || (item.standardLevelFaultsF3 != null)) {
                    isVisible = true
                }
            }
            "G" -> {
                if (item.standardLevelFaultsG1 != null || item.standardLevelFaultsG2 != null || (item.standardLevelFaultsG3 != null)) {
                    isVisible = true
                }
            }
            "H" -> {
                if (item.standardLevelFaultsH1 != null || item.standardLevelFaultsH2 != null || (item.standardLevelFaultsH3 != null)) {
                    isVisible = true
                }
            }
            "I" -> {
                if (item.standardLevelFaultsI1 != null || item.standardLevelFaultsI2 != null || (item.standardLevelFaultsI3 != null)) {
                    isVisible = true
                }
            }
            "J" -> {
                if (item.standardLevelFaultsJ1 != null || item.standardLevelFaultsJ2 != null || (item.standardLevelFaultsJ3 != null)) {
                    isVisible = true
                }
            }
        }
        return isVisible
    }
}