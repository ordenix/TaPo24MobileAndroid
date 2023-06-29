package pl.tapo24.infrastructure

import pl.tapo24.data.Uid
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface InterfaceNetworkClient {
    @Headers(
        "Content-Type: application/json; charset=utf-8"
    )
    @GET("installation/get_UID")
    fun getInstalaationUid(): Call<Uid>
}