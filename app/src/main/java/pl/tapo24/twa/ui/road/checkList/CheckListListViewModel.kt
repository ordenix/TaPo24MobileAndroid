package pl.tapo24.twa.ui.road.checkList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.tapo24.twa.adapter.CheckListListAdapter
import pl.tapo24.twa.db.entity.CheckListType
import pl.tapo24.twa.useCase.checkList.GetCheckListAllTypeUseCase
import javax.inject.Inject

@HiltViewModel
class CheckListListViewModel @Inject constructor(
    private val getCheckListAllTypeUseCase: GetCheckListAllTypeUseCase
): ViewModel() {

    val checkListAllType = MutableLiveData<List<CheckListType>>()
    lateinit var adapter: CheckListListAdapter
    val selectedCheckListTypeId = MutableLiveData<Int>(0)

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = getCheckListAllTypeUseCase.returnCheckListAllType()
            checkListAllType.postValue(list)
        }

    }

    fun selectCheckListType(id: Int) {
        selectedCheckListTypeId.value = id
    }

}