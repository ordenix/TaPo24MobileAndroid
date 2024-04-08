package pl.tapo24.twa.infrastructure

import pl.tapo24.twa.data.RType
import pl.tapo24.twa.data.survey.Survey
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface InterfaceNetworkClientAdmin {

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("admin/survey/get_all_survey")
    fun getSurveyList(@Header("Authorization")header: String): Call<RType<List<Survey>>>
}