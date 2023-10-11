package pl.tapo24.twa.ui.story

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.dbData.entity.Story
import javax.inject.Inject


@HiltViewModel
class StoryViewModel @Inject constructor(
    private val dataTapoDb: DataTapoDb
): ViewModel() {

    val data = MutableLiveData<List<Story>>()
    val currentStep = MutableLiveData<Story>()
    var dataInit = false
    val stepsArray: MutableList<Int> = mutableListOf()
    var storyType: String = ""
    fun getData(){
        viewModelScope.launch(Dispatchers.IO){
            var dataFromDb: List<Story>? = null
            async { dataFromDb = dataTapoDb.story().getAllByType(storyType) }.await()
            withContext(Dispatchers.Main) {
                if (dataFromDb != null) {
                    data.value = dataFromDb!!
                }
            }
        }

    }

    fun setData(step: Int){
        stepsArray.add(step)
        currentStep.value = data.value?.find { element -> element.link == step }
    }

    fun stepBack() {
        if (stepsArray.size > 1 ){
            stepsArray.removeLast()
            currentStep.value = data.value?.find { element -> element.link == stepsArray.last() }
        }
    }
}