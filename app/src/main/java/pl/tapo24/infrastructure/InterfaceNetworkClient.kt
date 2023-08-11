package pl.tapo24.infrastructure

import pl.tapo24.data.Uid
import pl.tapo24.dbData.entity.CodeDrivingLicence
import pl.tapo24.dbData.entity.DataBaseVersion
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface InterfaceNetworkClient {
    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("installation/get_UID")
    fun getInstallationUid(): Call<Uid>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("data/data_base_version_by_base_name")
    fun getDataBaseVersionByName(@Query("name") name: String): Call<DataBaseVersion>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("data/all_data_base_version")
    fun getAllDataBaseVersion(): Call<List<DataBaseVersion>>


    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("data/data_code_driving_licence")
    fun getDrivingLicenceCodeData(): Call<List<CodeDrivingLicence>>
}