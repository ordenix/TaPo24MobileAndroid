package pl.tapo24.infrastructure

import pl.tapo24.data.Uid
import pl.tapo24.dbData.entity.*
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


    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("data/data_country_driving_licence")
    fun getCountryDrivingLicenceData(): Call<List<CountryDrivingLicence>>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("data/data_lights_code_country")
    fun getLightsCodeData(): Call<List<LightsCodeCountry>>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("data/data_lights_front")
    fun getLightsFrontData(): Call<List<LightsFront>>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("data/data_lights_others")
    fun getLightsOthersData(): Call<List<LightsOthers>>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("data/data_status")
    fun getStatusData(): Call<List<Status>>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("data/data_towing")
    fun getTowingData(): Call<List<Towing>>
//
    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("data/data_code_limits_driving_licence")
    fun getCodeLimitsDrivingLicence(): Call<List<CodeLimitsDrivingLicence>>
    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("data/data_code_points_new")
    fun getCodePointsNew(): Call<List<CodePointsNew>>
    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("data/data_code_points_old")
    fun getCodePointsOld(): Call<List<CodePointsOld>>
    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("data/data_control_list")
    fun getControlList(): Call<List<ControlList>>
    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("data/data_holding_documents")
    fun getHoldingDocuments(): Call<List<HoldingDocuments>>
    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("data/data_story")
    fun getStory(): Call<List<Story>>
}