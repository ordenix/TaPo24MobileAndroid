package pl.tapo24.infrastructure

import okhttp3.OkHttpClient
import pl.tapo24.data.Uid
import pl.tapo24.dbData.entity.*
import pl.tapo24.exceptions.InternalException
import pl.tapo24.exceptions.InternalMessage
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkClient(var url: String) {

    private val client: OkHttpClient = OkHttpClient.Builder().apply {

    }.build()


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
        return Result.failure(InternalException(InternalMessage.InternalGetHoldingDocuments.message))
    }



    fun login() {

    }

}