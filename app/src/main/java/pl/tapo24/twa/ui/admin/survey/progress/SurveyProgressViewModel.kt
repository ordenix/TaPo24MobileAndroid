package pl.tapo24.twa.ui.admin.survey.progress

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.adapter.SurveyProgressAnswerAdapter
import pl.tapo24.twa.data.survey.ProgressData
import pl.tapo24.twa.data.survey.Survey
import pl.tapo24.twa.data.survey.SurveyId
import pl.tapo24.twa.infrastructure.NetworkClientAdmin
import pl.tapo24.twa.utils.DataFormatterConverter
import javax.inject.Inject

@HiltViewModel
class SurveyProgressViewModel @Inject constructor(
    val networkClientAdmin: NetworkClientAdmin
): ViewModel() {
    var survey: Survey = Survey()
    val progress: MutableLiveData<ProgressData> = MutableLiveData()
    lateinit var adapter: SurveyProgressAnswerAdapter
    val moveBack: MutableLiveData<Boolean> = MutableLiveData(false)




    fun getSurveyProgress() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = async {networkClientAdmin.getSurveyProgress(survey.id!!)}.await()
            response.onSuccess {
                withContext(Dispatchers.Main) {
                    progress.value= it
                }
            }
        }
    }

    fun changeSurveyStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = async {networkClientAdmin.changeSurveyStatus(SurveyId(survey.id!!))}.await()
            response.onSuccess {
                withContext(Dispatchers.Main) {
                    moveBack.value = true
                }
            }
            response.onFailure {
                withContext(Dispatchers.Main) {
                    moveBack.value = true
                }
            }
        }
    }
}