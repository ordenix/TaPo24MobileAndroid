package pl.tapo24.infrastructure

import okhttp3.OkHttpClient
import pl.tapo24.data.State
import pl.tapo24.data.elastic.DataQueryFromSuggestion
import pl.tapo24.data.elastic.DataQueryToSuggestion
import pl.tapo24.data.elastic.queryToElasticForTariffList.DataToElasticForTariffList
import pl.tapo24.data.elastic.resultClasFromElastic.DataTariffListFromElastic
import pl.tapo24.exceptions.InternalException
import pl.tapo24.exceptions.InternalMessage
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
            // TODO: BEARER
            val response = service.getTariffList(suggestions, State.enginesType.bearer).execute()
            println(response)
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
            // TODO: BEARER
            val response = service.getQuerySuggestion(suggestions, State.enginesType.bearer).execute()
            println(response)
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetElastic.message))
    }









}