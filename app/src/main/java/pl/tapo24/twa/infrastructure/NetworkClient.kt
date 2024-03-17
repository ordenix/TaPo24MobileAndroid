package pl.tapo24.twa.infrastructure

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pl.tapo24.twa.data.DataHomeNews
import pl.tapo24.twa.data.RType
import pl.tapo24.twa.data.State
import pl.tapo24.twa.data.Uid
import pl.tapo24.twa.data.checkListMap.CheckListMapComplex
import pl.tapo24.twa.data.login.DataUser
import pl.tapo24.twa.data.login.RequestLoginViaGoogle
import pl.tapo24.twa.data.login.ToLoginData
import pl.tapo24.twa.data.postal.ResponseCity
import pl.tapo24.twa.data.postal.ResponseCityWithStreetNumber
import pl.tapo24.twa.data.postal.ResponseCodeSequence
import pl.tapo24.twa.data.profile.BodyOffenses
import pl.tapo24.twa.db.entity.*
import pl.tapo24.twa.dbData.entity.*
import pl.tapo24.twa.exceptions.HttpException
import pl.tapo24.twa.exceptions.HttpMessage
import pl.tapo24.twa.exceptions.InternalException
import pl.tapo24.twa.exceptions.InternalMessage
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.KClass
import kotlin.reflect.cast


class NetworkClient(var url: String) {

    private val client: OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor{
                chain ->
            val request = chain.request()
            val response = chain.proceed(request)
            if (response.code() == 401) {
                println("ssss")
            }
            response
        }
    }.build()
//    val gson = GsonBuilder()
//        .setLenient()
//        .create()


    private var retro = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private var service = retro.create(InterfaceNetworkClient::class.java)

    fun rebuild(url: String) {
        retro = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        service = retro.create(InterfaceNetworkClient::class.java)

    }



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

    fun getAllDataBaseVersion(): Result<List<DataBaseVersion>> {
        try {
            val response = service.getAllDataBaseVersion(State.uid).execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetBaseVersion.message))
    }



    fun getDynamicData(path: String): Result<List<Any>> {
        try {
            val response = service.getDynamicData(State.uid,"data/$path").execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetData.message))
    }

    fun getHomeNewsData(): Result<List<DataHomeNews>>{
        try {
            val response = service.getHomeNews(State.uid).execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
            println(response)
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetHomeNewsData.message))
    }



    fun getLawData(): Result<List<Law>>{
        try {
            val response: Response<List<Law>> = if (State.jwtToken.isNotEmpty()) {
                // not empty
                service.getLaw("Bearer ${State.jwtToken}", State.uid).execute()
            } else {
                // empty
                service.getLaw(State.uid).execute()
            }
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetLawDocuments.message))
    }

    fun getTariffData(): Result<List<Tariff>>{
        try {
            val response = service.getTariffData(State.uid).execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetLawTariff.message))
    }

    fun getAssetListData(): Result<List<AssetList>>{
        try {
            val response = service.getAssetListData(State.uid).execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetAssetList.message))
    }

    fun getAppVersionData(): Result<List<AppVersion>>{
        try {
            val response = service.getAppVersionData(State.uid).execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetAppVersion.message))
    }


    fun getMourningData(): Result<List<Mourning>>{
        try {
            val response = service.getMourningData(State.uid).execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetMourning.message))
    }


    fun getPostalCodeSequenceByCity(city: String): Result<ResponseCodeSequence>{
        try {
            val response = service.getPostalCodeSequenceByCity(State.uid,city).execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }else if (response.code() == 404) {
                return Result.failure(HttpException(HttpMessage.PostalCityNotFound.message))
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetPostal.message))
    }

    fun getPostalCityByCode(code: String): Result<ResponseCity>{
        try {
            val response = service.getPostalCityByCode(State.uid,code).execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }else if (response.code() == 404) {
                return Result.failure(HttpException(HttpMessage.PostalCodeNotFound.message))
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetPostal.message))
    }

    fun getListProvinceByVoivodeship(voivodeship: String): Result<List<ResponseCityWithStreetNumber>>{
        try {
            val response = service.getListProvinceByVoivodeship(State.uid,voivodeship).execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!.r)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetPostal.message))
    }

    fun getListVoivodeship(voivodeship: String): Result<List<ResponseCityWithStreetNumber>>{
        try {
            val response = service.getListVoivodeship(State.uid, voivodeship).execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!.r)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetPostal.message))
    }

    fun getListCityByCommunity(community: String): Result<List<ResponseCityWithStreetNumber>>{
        try {
            val response = service.getListCityByCommunity(State.uid, community).execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!.r)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetPostal.message))
    }

    fun getListCommunityByProvince(province: String): Result<List<ResponseCityWithStreetNumber>>{
        try {
            val response = service.getListCommunityByProvince(State.uid, province).execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!.r)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetPostal.message))
    }

    fun getPostalCodeByParameters(
        voivodeship: String,
        province: String,
        community: String,
        city: String,
        street: String,
        number: String
    ): Result<List<ResponseCityWithStreetNumber>>{
        try {
            val response = service.getPostalCodeByParameters(State.uid,voivodeship, province, community, city, street, number).execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!.r)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetPostal.message))
    }

    // login

    fun promoteToPaidAccount(): Result<String> {
        return try {
            val response = service.promoteToPaidAccount("Bearer ${State.jwtToken}", State.uid).execute()
            if (response.isSuccessful) {
                Result.success(response.body()!!.r!!)
            } else {
                val errorMessage = response.errorBody()?.string()
                Result.failure(HttpException(errorMessage))
            }
        } catch (ex: Throwable) {
            Result.failure(ex)
        }
    }

    fun getDataUser(): Result<DataUser> {
        try {
            val response = service.getDataUser(State.jwtToken, State.uid).execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetUserData.message))
    }
    fun login(loginData: ToLoginData): Result<String> {
        try {
            val response = service.basicLogin(State.uid,loginData).execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            } else {
                val errorMessage = response.errorBody()?.string()
                if (response.code() == 409 && errorMessage == "User not found") {
                    // not found
                    return Result.failure(HttpException(HttpMessage.WrongPasswordOrAccountNotFound.message))

                }
                else if (response.code() == 409 && errorMessage == "User was banned") {
                    // baned
                    return Result.failure(HttpException(HttpMessage.AccountWasBanned.message))

                }
                else if (response.code() == 409 && errorMessage == "User have not activated account") {
                    // notActivated
                    return Result.failure(HttpException(HttpMessage.AccountNotActivated.message))

                }else if (response.code() == 409 && errorMessage == "User haven't confirmed email") {
                    // not email confirm
                    return Result.failure(HttpException(HttpMessage.AccountNotEmailConfirmed.message))

                }
                else if (response.code() == 401) {
                    // wrong password
                    return Result.failure(HttpException(HttpMessage.WrongPasswordOrAccountNotFound.message))
                }
            }



        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalLogin.message))
    }

    fun loginViaGoogle(googleToken: String): Result<String> {
        try {
            val response = service.loginViaGoogle(State.uid,RequestLoginViaGoogle(googleToken)).execute()
            if  (response.isSuccessful) {
                return Result.success(response.body()!!.r)
            } else {
                val errorMessage = response.errorBody()?.string()
                if (response.code() == 404) {
                    // user not found
                    return Result.failure(HttpException("404"))
                } else if(response.code() == 401) {
                    // bad verify token google
                    return Result.failure(HttpException(HttpMessage.BadVerifyGoogleAccount.message))
                } else if (response.code() == 403 && errorMessage == "User has been disabled") {
                    return Result.failure(HttpException(HttpMessage.UserNotHaveActivateAccount.message))

                } else if (response.code() == 403 && errorMessage == "User was banned") {
                    return Result.failure(HttpException(HttpMessage.UserWasBanned.message))

                }
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalLogin.message))
    }
    fun generateNewToken(token: String): Result<String> {
        try {
            val response = service.generateNewToken("Bearer $token", State.uid).execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!.r!!)
            } else {
                val errorMessage = response.errorBody()?.string()
                return Result.failure(HttpException(errorMessage))
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalLogin.message))
    }

    fun checkValidToken(token: String): Result<String> {
        try {
            val response = service.checkValidToken("Bearer $token", State.uid).execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            } else if (response.code() == 500) {
                return Result.failure(HttpException(HttpMessage.TokenExpired.message))
            }

        }
        catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalTestToken.message))
    }

    fun getFavoritesOffenses(token: String): Result<BodyOffenses> {
        try {
            val response = service.getFavoritesOffenses("Bearer $token", State.uid).execute()
            return if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(HttpException(response.errorBody().toString()))
            }
        }
        catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalFavOffenseToken.message))
    }

    fun putFavoritesOffenses(token: String, data: BodyOffenses):Result<String> {
        try {
            val response = service.putFavoritesOffenses("Bearer $token",State.uid, data).execute()
            return if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(HttpException(response.errorBody().toString()))
            }
        }
        catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalFavOffenseToken.message))
    }
    // customCategory

    fun getCustomCategoryList(token: String):Result<List<CustomCategory>> {
        try {
            val response = service.getCustomCategoryList("Bearer $token", State.uid).execute()
            return  if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(HttpException(response.errorBody().toString()))
            }

        }catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalCustomCategory.message))

    }

    fun getCustomCategoryMapList(token: String):Result<List<MapCategory>> {
        try {
            val response = service.getCustomMapList("Bearer $token", State.uid).execute()
            return  if (response.isSuccessful) {
                Result.success(response.body()!!.r)
            } else {
                Result.failure(HttpException(response.errorBody().toString()))
            }

        }catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalCustomCategoryMap.message))

    }

    fun putCustomCategory(token: String, data: CustomCategory):Result<CustomCategory> {
        try {
            val response = service.putCustomCategory("Bearer $token", State.uid, data).execute()
            return  if (response.isSuccessful) {
                Result.success(response.body()!!.r)
            } else {
                val errorMessage = response.errorBody()?.string()
                 if (response.code() == 409 && errorMessage == "User have already that custom category name") {
                    return Result.failure(HttpException(HttpMessage.UserHaveAlreadyThatCustomCategory.message))
                }
                else {
                    return Result.failure(HttpException(errorMessage))
                }

            }

        }catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalCustomCategory.message))

    }

    fun deleteCustomCategory(token: String, customCategory: CustomCategory):Result<String> {
        try {
            val response = service.deleteCustomCategory("Bearer $token", State.uid, customCategory).execute()
            return  if (response.isSuccessful) {
                Result.success(response.body()!!.r ?: "OK")
            } else {
                val errorMessage = response.errorBody()?.string()
                if (response.code() == 409 && errorMessage == "This custom category not exist") {
                    return Result.failure(HttpException(HttpMessage.ThisCustomCategoryNotExist.message))
                }
                else {
                    return Result.failure(HttpException(errorMessage))
                }
            }

        }catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalCustomCategory.message))

    }

    fun putCustomCategoryMap(token: String, mapCategory: MapCategory): Result<String> {
        try {
            val response = service.putCustomCategoryMap("Bearer $token",State.uid, mapCategory).execute()
            return  if (response.isSuccessful) {
                Result.success(response.body()!!.r ?: "OK")
            } else {
                val errorMessage = response.errorBody()?.string()
                return Result.failure(HttpException(errorMessage))

            }

        }catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalCustomCategoryMap.message))
    }

    fun deleteCustomCategoryMap(token: String, mapCategory: MapCategory): Result<String> {
        try {
            val response = service.deleteCustomCategoryMap("Bearer $token",State.uid, mapCategory).execute()
            return  if (response.isSuccessful) {
                Result.success(response.body()!!.r ?: "OK")
            } else {
                val errorMessage = response.errorBody()?.string()
                if (response.code() == 409 && errorMessage == "This custom map not exist") {
                    return Result.failure(HttpException(HttpMessage.ThisCustomCategoryMapNotExist.message))
                }
                else {
                    return Result.failure(HttpException(errorMessage))
                }

            }

        }catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalCustomCategoryMap.message))
    }

    fun getShopStatus(): Result<Setting> {
        try {
            val response = service.getShopStatus(State.uid).execute()
            return if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(HttpException(response.errorBody().toString()))
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
    }

    fun getCheckListDictionary(): Result<List<CheckListDictionary>> {
        try {
            val response = service.getCheckListDictionary("Bearer ${State.jwtToken}", State.uid).execute()
            return if (response.isSuccessful) {
                Result.success(response.body()!!.r)
            } else {
                Result.failure(HttpException(response.errorBody().toString()))
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
    }

    fun getCheckListAllMap(): Result<List<CheckListMapComplex>> {
        try {
            val response = service.getCheckListAllMap("Bearer ${State.jwtToken}", State.uid).execute()
            return if (response.isSuccessful) {
                Result.success(response.body()!!.r)
            } else {
                Result.failure(HttpException(response.errorBody().toString()))
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
    }

    fun getCheckListMapByTypeId(id: Int): Result<List<CheckListMapComplex>> {
        try {
            val response = service.getCheckListMapByTypeId("Bearer ${State.jwtToken}", State.uid, id).execute()
            return if (response.isSuccessful) {
                Result.success(response.body()!!.r)
            } else {
                Result.failure(HttpException(response.errorBody().toString()))
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
    }

    fun getAllTypeList(): Result<List<CheckListType>> {
        try {
            val response = service.getAllTypeList("Bearer ${State.jwtToken}", State.uid).execute()
            return if (response.isSuccessful) {
                Result.success(response.body()!!.r)
            } else {
                Result.failure(HttpException(response.errorBody().toString()))
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
    }

}