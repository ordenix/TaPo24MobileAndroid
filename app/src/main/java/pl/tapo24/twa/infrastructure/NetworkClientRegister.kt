package pl.tapo24.twa.infrastructure


import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.stream.MalformedJsonException
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import pl.tapo24.twa.data.State
import pl.tapo24.twa.data.register.RegisterForm
import pl.tapo24.twa.exceptions.HttpException
import pl.tapo24.twa.exceptions.HttpMessage
import pl.tapo24.twa.exceptions.InternalException
import pl.tapo24.twa.exceptions.InternalMessage
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkClientRegister(var url: String) {



    private val client: OkHttpClient = OkHttpClient.Builder().apply {
        connectTimeout(20, TimeUnit.SECONDS)
        writeTimeout(20, TimeUnit.SECONDS)
        readTimeout(20, TimeUnit.SECONDS)
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
        .baseUrl("${url}register/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private var service = retro.create(InterfaceNetworkClientRegister::class.java)

    fun rebuild(url: String) {
        retro = Retrofit.Builder()
            .baseUrl("${url}register/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        service = retro.create(InterfaceNetworkClientRegister::class.java)

    }

    fun checkLogin(data: RegisterForm): Result<String> {
        try {
            val response = service.checkExistLogin(State.uid,data).execute()
            if (response.isSuccessful) {
                return Result.success("Ok")
            } else {
                val errorMessage = response.errorBody()?.string()
                if (response.code() == 409 && errorMessage == "This login already used") {
                    //  found
                    return Result.failure(HttpException(HttpMessage.LoginExistInService.message))

                }
            }
        }  catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalRegister.message))
    }

    fun checkEmail(data: RegisterForm): Result<String> {
        try {
            val response = service.checkExistEmail(State.uid,data).execute()
            if (response.isSuccessful) {
                return Result.success("Ok")
            } else {
                val errorMessage = response.errorBody()?.string()
                if (response.code() == 409 && errorMessage == "This email already used") {
                    //  found
                    return Result.failure(HttpException(HttpMessage.EmailExistInService.message))

                }
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalRegister.message))
    }


    fun registerUser(data: RegisterForm): Result<String> {
        try {
            val response = service.basicRegisterUser(State.uid,data).execute()
            if (response.isSuccessful) {
                return Result.success("Ok")
            } else {
                val errorMessage = response.errorBody()?.string()
                return Result.failure(InternalException(errorMessage))
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
        return Result.failure(InternalException(InternalMessage.InternalRegister.message))
    }


}