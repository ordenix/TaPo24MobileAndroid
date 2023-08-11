package pl.tapo24.infrastructure

import okhttp3.OkHttpClient
import pl.tapo24.data.Uid
import pl.tapo24.dbData.entity.CodeDrivingLicence
import pl.tapo24.dbData.entity.DataBaseVersion
import pl.tapo24.exceptions.InternalException
import pl.tapo24.exceptions.InternalMessage
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
            val response = service.getInstallationUid().execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetUid.message))
    }


    fun getDataBaseVersionByName(name: String): Result<DataBaseVersion> {
        try {
            val response = service.getDataBaseVersionByName(name).execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetBaseVersion.message))
    }

    fun getCodeDrivingLicence(): Result<List<CodeDrivingLicence>> {
        try {
            val response = service.getDrivingLicenceCodeData().execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetCodeDrivingLicence.message))
    }

    fun getAllDataBaseVersion(): Result<List<DataBaseVersion>> {
        try {
            val response = service.getAllDataBaseVersion().execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetBaseVersion.message))
    }
    fun login() {

    }

}