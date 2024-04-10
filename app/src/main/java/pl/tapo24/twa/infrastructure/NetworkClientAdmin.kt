package pl.tapo24.twa.infrastructure

import okhttp3.OkHttpClient
import pl.tapo24.twa.data.State
import pl.tapo24.twa.data.survey.ProgressData
import pl.tapo24.twa.data.survey.Survey
import pl.tapo24.twa.data.survey.SurveyId
import pl.tapo24.twa.exceptions.InternalException
import pl.tapo24.twa.exceptions.InternalMessage
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkClientAdmin(var url: String) {
    private val client: OkHttpClient = OkHttpClient.Builder().apply {

    }.build()

    private var retro = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private var service = retro.create(InterfaceNetworkClientAdmin::class.java)

    fun rebuild(url: String) {
        retro = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        service = retro.create(InterfaceNetworkClientAdmin::class.java)
    }

    fun getSurveyList(): Result<List<Survey>> {
        try {
            val response = service.getSurveyList("Bearer ${State.jwtToken}").execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!.r)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetSurveyList.message))
    }

    fun sendSurvey(survey: Survey): Result<String> {
        try {
            val response = service.sendSurvey("Bearer ${State.jwtToken}", survey).execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!.r)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalSendSurvey.message))
    }

    fun getSurveyProgress(surveyId: Int): Result<ProgressData> {
        try {
            val response = service.getSurveyProgress("Bearer ${State.jwtToken}", surveyId).execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!.r)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetSurveyProgress.message))
    }

    fun changeSurveyStatus(surveyId: SurveyId): Result<String> {
        try {
            val response = service.changeSurveyStatus("Bearer ${State.jwtToken}", surveyId).execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!.r)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalChangeSurveyStatus.message))
    }
}