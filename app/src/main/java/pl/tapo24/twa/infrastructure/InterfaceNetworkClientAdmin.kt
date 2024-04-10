package pl.tapo24.twa.infrastructure

import pl.tapo24.twa.data.RType
import pl.tapo24.twa.data.survey.ProgressData
import pl.tapo24.twa.data.survey.Survey
import pl.tapo24.twa.data.survey.SurveyId
import retrofit2.Call
import retrofit2.http.*

interface InterfaceNetworkClientAdmin {

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("admin/survey/get_all_survey")
    fun getSurveyList(@Header("Authorization")header: String): Call<RType<List<Survey>>>



    @Headers("Content-Type: application/json; charset=utf-8")
    @PUT("admin/survey/put_survey")
    fun sendSurvey(@Header("Authorization")header: String, @Body survey: Survey): Call<RType<String>>


    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("admin/survey/get_progress_survey")
    fun getSurveyProgress(@Header("Authorization")header: String, @Query("surveyId") surveyId: Int,): Call<RType<ProgressData>>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("admin/survey/change_status_survey")
    fun changeSurveyStatus(@Header("Authorization")header: String, @Body data: SurveyId): Call<RType<String>>
}