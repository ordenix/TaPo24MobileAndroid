package pl.tapo24.infrastructure

import okhttp3.OkHttpClient
import pl.tapo24.data.Uid
import pl.tapo24.exceptions.InternalException
import pl.tapo24.exceptions.InternalMessage
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class NetworkClient(var url: String) {

    private val client: OkHttpClient = OkHttpClient.Builder().apply {

    }.build()


    val retro = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private var service = retro.create(InterfaceNetworkClient::class.java)

    fun getUid(): Result<Uid> {
        try {
            val response = service.getInstalaationUid().execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetUid.message))
    }

}