package pl.tapo24.twa.infrastructure

import pl.tapo24.twa.dbData.entity.*
import pl.tapo24.twa.data.Uid
import pl.tapo24.twa.data.login.ToLoginData
import pl.tapo24.twa.data.postal.ResponseCity
import pl.tapo24.twa.data.postal.ResponseCodeSequence
import pl.tapo24.twa.data.profile.BodyOffenses
import pl.tapo24.twa.db.entity.AppVersion
import pl.tapo24.twa.db.entity.AssetList
import pl.tapo24.twa.db.entity.Tariff
import pl.tapo24.twa.dbData.entity.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
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
    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("data/data_uto")
    fun getUto(): Call<List<Uto>>
    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("data/data_sign")
    fun getSign(): Call<List<Sign>>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("data/data_law")
    fun getLaw(): Call<List<Law>>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("data/data_tariff")
    fun getTariffData(): Call<List<Tariff>>


    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("data/data_asset_list")
    fun getAssetListData(): Call<List<AssetList>>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("data/data_app_version")
    fun getAppVersionData(): Call<List<AppVersion>>

    // endpoints for module
    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("postal/get_city_code_sequence_by_city_name/")
    fun getPostalCodeSequenceByCity(@Query("city") city: String): Call<ResponseCodeSequence>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("postal/get_city_by_code/")
    fun getPostalCityByCode(@Query("code") code: String): Call<ResponseCity>

    // endpoints for login
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("login/normal_login_user/")
    fun basicLogin(@Body login: ToLoginData): Call<String>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("login/test_token_valid_and_account_baned_status/")
    fun checkValidToken(@Header("Authorization")header: String): Call<String>
    // response profile
    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("profile/favorites_offenses/")
    fun getFavoritesOffenses(@Header("Authorization")header: String): Call<BodyOffenses>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @PUT("profile/favorites_offenses/")
    fun putFavoritesOffenses(@Header("Authorization")header: String, @Body data: BodyOffenses): Call<String>


}