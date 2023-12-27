package pl.tapo24.twa.infrastructure

import okhttp3.OkHttpClient
import pl.tapo24.twa.data.State
import pl.tapo24.twa.data.Uid
import pl.tapo24.twa.data.login.DataUser
import pl.tapo24.twa.data.login.ToLoginData
import pl.tapo24.twa.data.postal.ResponseCity
import pl.tapo24.twa.data.postal.ResponseCodeSequence
import pl.tapo24.twa.data.profile.BodyOffenses
import pl.tapo24.twa.db.entity.*
import pl.tapo24.twa.dbData.entity.*
import pl.tapo24.twa.exceptions.HttpException
import pl.tapo24.twa.exceptions.HttpMessage
import pl.tapo24.twa.exceptions.InternalException
import pl.tapo24.twa.exceptions.InternalMessage
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


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

    fun getCountryDrivingLicenceData(): Result<List<CountryDrivingLicence>> {
        try {
            val response = service.getCountryDrivingLicenceData().execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetCountry.message))
    }

    fun getLightsCodeData(): Result<List<LightsCodeCountry>> {
        try {
            val response = service.getLightsCodeData().execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetLightsCode.message))
    }

    fun getLightsFrontData(): Result<List<LightsFront>> {
        try {
            val response = service.getLightsFrontData().execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetFrontLights.message))
    }

    fun getLightsOthersData(): Result<List<LightsOthers>>{
        try {
            val response = service.getLightsOthersData().execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetLightsOther.message))
    }

    fun getStatusData(): Result<List<Status>>{
        try {
            val response = service.getStatusData().execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetStatus.message))
    }

    fun getTowingData(): Result<List<Towing>>{
        try {
            val response = service.getTowingData().execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetTowing.message))
    }
    //

    fun getStoryData(): Result<List<Story>>{
        try {
            val response = service.getStory().execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetStory.message))
    }

    fun getControlListData(): Result<List<ControlList>>{
        try {
            val response = service.getControlList().execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetControlList.message))
    }

    fun getCodePointsOldData(): Result<List<CodePointsOld>>{
        try {
            val response = service.getCodePointsOld().execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetPointsOld.message))
    }

    fun getCodePointsNewData(): Result<List<CodePointsNew>>{
        try {
            val response = service.getCodePointsNew().execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetPointsNew.message))
    }

    fun getCodeLimitsDrivingLicenceData(): Result<List<CodeLimitsDrivingLicence>>{
        try {
            val response = service.getCodeLimitsDrivingLicence().execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetCodeLimits.message))
    }

    fun getHoldingDocumentsData(): Result<List<HoldingDocuments>>{
        try {
            val response = service.getHoldingDocuments().execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetHoldingDocuments.message))
    }

    fun getUtoData(): Result<List<Uto>>{
        try {
            val response = service.getUto().execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetUtoDocuments.message))
    }

    fun getSignData(): Result<List<Sign>>{
        try {
            val response = service.getSign().execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetSignDocuments.message))
    }

    fun getLawData(): Result<List<Law>>{
        try {
            val response = service.getLaw().execute()
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
            val response = service.getTariffData().execute()
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
            val response = service.getAssetListData().execute()
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
            val response = service.getAppVersionData().execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetAppVersion.message))
    }

    fun getSpbData(): Result<List<Spb>>{
        try {
            val response = service.getSpbData().execute()
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalGetSpb.message))
    }

    fun getMourningData(): Result<List<Mourning>>{
        try {
            val response = service.getMourningData().execute()
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
            val response = service.getPostalCodeSequenceByCity(city).execute()
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
            val response = service.getPostalCityByCode(code).execute()
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

    // login

    fun getDataUser(): Result<DataUser> {
        try {
            val response = service.getDataUser(State.jwtToken).execute()
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
            val response = service.basicLogin(loginData).execute()
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

    fun checkValidToken(token: String): Result<String> {
        try {
            val response = service.checkValidToken("Bearer $token").execute()
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
            val response = service.getFavoritesOffenses("Bearer $token").execute()
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
            val response = service.putFavoritesOffenses("Bearer $token", data).execute()
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
            val response = service.getCustomCategoryList("Bearer $token").execute()
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
            val response = service.getCustomMapList("Bearer $token").execute()
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
            val response = service.putCustomCategory("Bearer $token", data).execute()
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
            val response = service.deleteCustomCategory("Bearer $token", customCategory).execute()
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
            val response = service.putCustomCategoryMap("Bearer $token", mapCategory).execute()
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
            val response = service.deleteCustomCategoryMap("Bearer $token", mapCategory).execute()
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

}