package pl.tapo24.infrastructure

import okhttp3.OkHttpClient
import pl.tapo24.data.Uid
import pl.tapo24.data.elastic.DataQueryFromSuggestion
import pl.tapo24.data.elastic.DataQueryToSuggestion
import pl.tapo24.dbData.entity.*
import pl.tapo24.exceptions.InternalException
import pl.tapo24.exceptions.InternalMessage
import retrofit2.Call
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






    fun getSuggestionList(suggestions: DataQueryToSuggestion): Result<DataQueryFromSuggestion> {
        try {
            // TODO: BEARER
            val response = service.getQuerySuggestion(suggestions, "Bearer search-gwuqjnb5u2je9vnc6e7i5gnd").execute()
            println(response)
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetBaseVersion.message))
    }









}