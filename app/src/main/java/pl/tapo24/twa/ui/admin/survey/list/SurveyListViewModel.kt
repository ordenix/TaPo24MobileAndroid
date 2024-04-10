package pl.tapo24.twa.ui.admin.survey.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.adapter.SurveyListAdapter
import pl.tapo24.twa.data.survey.Survey
import pl.tapo24.twa.infrastructure.NetworkClientAdmin
import javax.inject.Inject

@HiltViewModel
class SurveyListViewModel @Inject constructor(
    private val networkClientAdmin: NetworkClientAdmin
): ViewModel() {
    val surveyList: MutableLiveData<List<Survey>> = MutableLiveData()
    val selectedSurvey: MutableLiveData<Survey> = MutableLiveData()
    lateinit var adapter: SurveyListAdapter

    init {
        getSurveyList()
    }

    fun selectSurvey(survey: Survey) {
        selectedSurvey.value = survey
    }

    fun getSurveyList() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = async{networkClientAdmin.getSurveyList()}.await()
            withContext(Dispatchers.Main) {
                surveyList.value = response.getOrNull()
            }
        }
    }

}