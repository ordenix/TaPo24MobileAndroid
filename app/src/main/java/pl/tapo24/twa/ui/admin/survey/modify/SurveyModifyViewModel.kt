package pl.tapo24.twa.ui.admin.survey.modify

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.adapter.SurveyAnswerEditModeAdapter
import pl.tapo24.twa.data.survey.Survey
import pl.tapo24.twa.data.survey.SurveyAnswer
import pl.tapo24.twa.infrastructure.NetworkClientAdmin
import pl.tapo24.twa.utils.DataFormatterConverter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SurveyModifyViewModel @Inject constructor(
    private val networkClientAdmin: NetworkClientAdmin
): ViewModel(), DataFormatterConverter {
    lateinit var adapter: SurveyAnswerEditModeAdapter
    val showLoader: MutableLiveData<Boolean> = MutableLiveData(false)
    val moveBack: MutableLiveData<Boolean> = MutableLiveData(false)



    var dataModel: Survey = Survey()
    var name: String? = null
    var dateStart: String? = null
    var dateEnd: String? = null
    var activate: Boolean = false
    var singleChoice = true
    var toVipUser: Boolean? = null
    var toLoginUser: Boolean? = null

    val answerList: MutableLiveData<MutableList<SurveyAnswer>> = MutableLiveData()

    fun addAnswer(answer: String) {
        val list = answerList.value ?: mutableListOf()
        list.add(SurveyAnswer(answer, sortId = 100))
        answerList.value = list
    }

    fun removeAnswer(answer: SurveyAnswer) {
        val list = answerList.value ?: mutableListOf()
        list.remove(answer)
        answerList.value = list
    }

    fun changeOrder() {
        val list = answerList.value ?: mutableListOf()
        list.forEachIndexed { index, surveyAnswer ->
            surveyAnswer.sortId = index
        }
        answerList.value = list
    }

    fun saveSurvey(publish: Boolean = false){
        showLoader.value = true
        dataModel.name = name
        dataModel.startDate = convertHumanCalendarDateToTimeStamp(dateStart!!).toLong()
        dataModel.endDate = convertHumanCalendarDateToTimeStamp(dateEnd!!).toLong()
        dataModel.active = activate
        dataModel.publish = publish
        dataModel.singleChoice = singleChoice
        dataModel.toVipUser = toVipUser
        dataModel.toLoginUser = toLoginUser
        dataModel.surveyAnswers = answerList.value?.toMutableList()?.let { ArrayList(it) }
        viewModelScope.launch(Dispatchers.IO) {
            val result = networkClientAdmin.sendSurvey(dataModel)
            result.onSuccess {
                withContext(Dispatchers.Main) {
                    showLoader.value = false
                    moveBack.value = true
                }
            }
            result.onFailure {
                withContext(Dispatchers.Main) {
                    showLoader.value = false
                    moveBack.value = true
                }
            }

        }
    }





}