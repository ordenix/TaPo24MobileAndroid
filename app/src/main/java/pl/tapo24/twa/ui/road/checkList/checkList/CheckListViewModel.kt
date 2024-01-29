package pl.tapo24.twa.ui.road.checkList.checkList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pl.tapo24.twa.adapter.CheckListAdapter
import pl.tapo24.twa.data.checkListMap.CheckListComplex
import pl.tapo24.twa.useCase.checkList.ChangeMapStateUseCase
import pl.tapo24.twa.useCase.checkList.GetCheckListAllTypeUseCase
import pl.tapo24.twa.useCase.checkList.GetComplexCheckListByType
import javax.inject.Inject

@HiltViewModel
class CheckListViewModel @Inject constructor(
    private val getComplexCheckListByType: GetComplexCheckListByType,
    private val changeMapStateUseCase: ChangeMapStateUseCase
): ViewModel() {
    var idList: Int? = null
    lateinit var adapter: CheckListAdapter
    val items = MutableLiveData<MutableList<CheckListComplex>>()
    val progress = MutableLiveData<Int>(0)
    val isEditMode = MutableLiveData<Boolean>(false)

    val invokeDeletedItem = MutableLiveData<CheckListComplex>()
    var invokeDeletedPosition = 0

    fun getCheckListByType(type: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            var  itemsFromDb: List<CheckListComplex>? = null
           async { itemsFromDb = (getComplexCheckListByType.getCheckListByTypeNonDeleted(type)) }.await()
            if (itemsFromDb != null) {
                items.postValue(itemsFromDb!!.toMutableList())
                if (itemsFromDb?.size != 0) {
                    val percent = itemsFromDb?.filter { it.isSelected }?.size?.times(100)?.div(itemsFromDb?.size ?: 1)
                    progress.postValue(percent?: 0)
                }

            }

        }
    }

    fun clickItem(item: CheckListComplex, position: Int) {
        item.isSelected = !item.isSelected
        changeMapStateUseCase.updateItem(item)
        adapter.notifyItemChanged(position)
        val percent = items.value?.filter { it.isSelected }?.size?.times(100)?.div(items.value?.size ?: 1)
        progress.postValue(percent?: 0)

    }

    fun clickEditMode() {
        isEditMode.value = !(isEditMode.value ?: false)
        adapter.notifyDataSetChanged()
    }

    fun unselectAll() {
        items.value?.forEach {
            it.isSelected = false
        }
        adapter.notifyDataSetChanged()
        progress.postValue(0)
        changeMapStateUseCase.unSelectAll(idList ?: 0)
    }

    fun invokeDeleteItem(item: CheckListComplex, position: Int) {
        invokeDeletedPosition = position
        invokeDeletedItem.postValue(item)
    }

    fun deleteItem(item: CheckListComplex, position: Int) {
        items.value?.remove(item)
        item.isDeleted = true
        changeMapStateUseCase.updateItem(item)
        adapter.notifyItemRemoved(position)
        val percent = items.value?.filter { it.isSelected }?.size?.times(100)?.div(items.value?.size ?: 1)
        progress.postValue(percent?: 0)
    }

    fun restoreList() {
        viewModelScope.launch(Dispatchers.IO) {
            changeMapStateUseCase.restoreList(idList ?: 0)
            getCheckListByType(idList ?: 0)
        }
    }




}