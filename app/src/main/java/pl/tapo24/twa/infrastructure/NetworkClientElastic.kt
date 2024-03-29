package pl.tapo24.twa.infrastructure

import okhttp3.OkHttpClient
import pl.tapo24.twa.data.State
import pl.tapo24.twa.data.elastic.DataClickToElastic
import pl.tapo24.twa.data.elastic.DataQueryFromSuggestion
import pl.tapo24.twa.data.elastic.DataQueryToSuggestion
import pl.tapo24.twa.data.elastic.queryToElasticForTariffList.DataToElasticForTariffList
import pl.tapo24.twa.data.elastic.resultClasFromElastic.DataTariffListFromElastic
import pl.tapo24.twa.exceptions.InternalException
import pl.tapo24.twa.exceptions.InternalMessage
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkClientElastic(var url: String) {

    private val client: OkHttpClient = OkHttpClient.Builder().apply {

    }.build()

    private var retro = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private var service = retro.create(InterfaceNetworkClientElastic::class.java)

    fun rebuild(url: String) {
        retro = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        service = retro.create(InterfaceNetworkClientElastic::class.java)
    }



    fun getTariffDataList(suggestions: DataToElasticForTariffList): Result<DataTariffListFromElastic> {
        try {
            val response = service.getTariffList(suggestions, State.enginesType.bearer).execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetElastic.message))
    }


    fun getSuggestionList(suggestions: DataQueryToSuggestion): Result<DataQueryFromSuggestion> {
        try {
            val response = service.getQuerySuggestion(suggestions, State.enginesType.bearer).execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetElastic.message))
    }

    fun clickData(dataToClick: DataClickToElastic): Result<Any> {
        try {
            val response = service.clickOnSuggestion(dataToClick, State.enginesType.bearer).execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetElastic.message))
    }









}