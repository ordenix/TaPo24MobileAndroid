package pl.tapo24.twa.infrastructure

import pl.tapo24.twa.data.elastic.DataQueryFromSuggestion
import pl.tapo24.twa.data.elastic.DataQueryToSuggestion
import pl.tapo24.twa.data.elastic.queryToElasticForTariffList.DataToElasticForTariffList
import pl.tapo24.twa.data.elastic.resultClasFromElastic.DataTariffListFromElastic
import pl.tapo24.twa.dbData.entity.*
import retrofit2.Call
import retrofit2.http.*

interface InterfaceNetworkClientElastic {


    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("query_suggestion")
    fun getQuerySuggestion(@Body body: DataQueryToSuggestion, @Header("Authorization") authorization:String): Call<DataQueryFromSuggestion>


    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("search.json")
    fun getTariffList(@Body body: DataToElasticForTariffList, @Header("Authorization") authorization:String): Call<DataTariffListFromElastic>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("click")
    fun clickOnSuggestion(@Body body: DataToElasticForTariffList, @Header("Authorization") authorization:String): Call<Any>

   // Bearer search-gwuqjnb5u2je9vnc6e7i5gnd -before
    //Bearer search-gwuqjnb5u2je9vnc6e7i5gnd new
    //https://elastic-enterprise-search.server.tapo24.pl/api/as/v1/engines/tapo24new/click
    //https://elastic-enterprise-search.server.tapo24.pl/api/as/v1/engines/tapo24new/search.json
    //https://elastic-enterprise-search.server.tapo24.pl/api/as/v1/engines/tapo24new/query_suggestion
}