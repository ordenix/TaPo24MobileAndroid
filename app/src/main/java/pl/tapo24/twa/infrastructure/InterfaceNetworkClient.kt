package pl.tapo24.twa.infrastructure

import pl.tapo24.twa.data.DataHomeNews
import pl.tapo24.twa.data.RString
import pl.tapo24.twa.data.RType
import pl.tapo24.twa.data.customCategory.RCustomMapList
import pl.tapo24.twa.dbData.entity.*
import pl.tapo24.twa.data.Uid
import pl.tapo24.twa.data.checkListMap.CheckListMapComplex
import pl.tapo24.twa.data.customCategory.RCustomCategory
import pl.tapo24.twa.data.login.DataUser
import pl.tapo24.twa.data.login.RequestLoginViaGoogle
import pl.tapo24.twa.data.login.ToLoginData
import pl.tapo24.twa.data.postal.ResponseCity
import pl.tapo24.twa.data.postal.ResponseCityWithStreetNumber
import pl.tapo24.twa.data.postal.ResponseCodeSequence
import pl.tapo24.twa.data.profile.BodyOffenses
import pl.tapo24.twa.db.entity.*
import retrofit2.Call
import retrofit2.http.*

interface InterfaceNetworkClient {
    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("installation/get_UID")
    fun getInstallationUid(): Call<Uid>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("data/all_data_base_version")
    fun getAllDataBaseVersion(): Call<List<DataBaseVersion>>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("{fullUrl}")
    fun getDynamicData(@Path(value = "fullUrl", encoded = true) fullUrl: String): Call<List<Any>>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("data/data_law")
    fun getLaw(@Header("Authorization")header: String): Call<List<Law>>

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


    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("data/data_mourning")
    fun getMourningData(): Call<List<Mourning>>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("data/shop_status")
    fun getShopStatus(): Call<Setting>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("data/data_home_page_news")
    fun getHomeNews(): Call<List<DataHomeNews>>


    // endpoints for module
    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("postal/get_city_code_sequence_by_city_name/")
    fun getPostalCodeSequenceByCity(@Query("city") city: String): Call<ResponseCodeSequence>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("postal/get_city_by_code/")
    fun getPostalCityByCode(@Query("code") code: String): Call<ResponseCity>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("postal/get_list_province_by_voivodeship/")
    fun getListProvinceByVoivodeship(@Query("voivodeship") voivodeship: String): Call<RType<List<ResponseCityWithStreetNumber>>>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("postal/get_list_voivodeship/")
    fun getListVoivodeship(@Query("voivodeship") voivodeship: String): Call<RType<List<ResponseCityWithStreetNumber>>>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("postal/get_list_city_by_community/")
    fun getListCityByCommunity(@Query("community") community: String): Call<RType<List<ResponseCityWithStreetNumber>>>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("postal/get_list_community_by_province/")
    fun getListCommunityByProvince(@Query("province") province: String): Call<RType<List<ResponseCityWithStreetNumber>>>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("postal/get_postal_code_by_parameters/")
    fun getPostalCodeByParameters(
        @Query("voivodeship") voivodeship: String,
        @Query("province") province: String,
        @Query("community") community: String,
        @Query("city") city: String,
        @Query("street") street: String,
        @Query("number") number: String
    ): Call<RType<List<ResponseCityWithStreetNumber>>>

    // endpoints for login
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("login/normal_login_user/")
    fun basicLogin(@Body login: ToLoginData): Call<String>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("login/login_via_google_auth_provider/")
    fun loginViaGoogle(@Body body: RequestLoginViaGoogle):  Call<RType<String>>


    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("login/test_token_valid_and_account_baned_status/")
    fun checkValidToken(@Header("Authorization")header: String): Call<String>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @POST("login/generate_new_JWT/")
    fun generateNewToken(@Header("Authorization")header: String): Call<RString>

    // response profile
    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("profile/favorites_offenses/")
    fun getFavoritesOffenses(@Header("Authorization")header: String): Call<BodyOffenses>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @PUT("profile/favorites_offenses/")
    fun putFavoritesOffenses(@Header("Authorization")header: String, @Body data: BodyOffenses): Call<String>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("login/get_data_user/")
    fun getDataUser(@Header("Authorization")header: String): Call<DataUser>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @POST("login/promote_to_paid_account/")
    fun promoteToPaidAccount(@Header("Authorization")header: String): Call<RString>

// customCategory

    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("custom_category/custom_category_list/")
    fun getCustomCategoryList(@Header("Authorization")header: String): Call<List<CustomCategory>>
    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("custom_category/custom_category_map_list/")
    fun getCustomMapList(@Header("Authorization")header: String): Call<RCustomMapList>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @PUT("custom_category/custom_category/")
    fun putCustomCategory(@Header("Authorization")header: String, @Body customCategory: CustomCategory): Call<RCustomCategory>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @HTTP(method = "DELETE", path = "custom_category/custom_category/", hasBody = true)
    fun deleteCustomCategory(@Header("Authorization")header: String, @Body customCategory: CustomCategory): Call<RString>


    @Headers("Content-Type: application/json; charset=UTF-8")
    @PUT("custom_category/custom_category_to_tariff/")
    fun putCustomCategoryMap(@Header("Authorization")header: String, @Body customMap: MapCategory): Call<RString>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @HTTP(method = "DELETE", path = "custom_category/custom_category_to_tariff/", hasBody = true)
    fun deleteCustomCategoryMap(@Header("Authorization")header: String, @Body customMap: MapCategory): Call<RString>
    // check list

    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("check_list/get_dictionary/")
    fun getCheckListDictionary(@Header("Authorization")header: String,): Call<RType<List<CheckListDictionary>>>


    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("check_list/get_all_map/")
    fun getCheckListAllMap(@Header("Authorization")header: String,): Call<RType<List<CheckListMapComplex>>>


    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("check_list/get_map_by_type_id/")
    fun getCheckListMapByTypeId(@Header("Authorization")header: String,@Query("code") id: Int): Call<RType<List<CheckListMapComplex>>>


    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("check_list/get_all_type_list/")
    fun getAllTypeList(@Header("Authorization")header: String,): Call<RType<List<CheckListType>>>
}