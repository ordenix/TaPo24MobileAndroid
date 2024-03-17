package pl.tapo24.twa.infrastructure


import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.stream.MalformedJsonException
import okhttp3.OkHttpClient
import pl.tapo24.twa.data.Dbid
import java.util.concurrent.TimeUnit
import pl.tapo24.twa.data.State
import pl.tapo24.twa.data.login.RequestLoginViaGoogle
import pl.tapo24.twa.data.login.ToLoginData
import pl.tapo24.twa.data.register.AccountTokenData
import pl.tapo24.twa.data.register.RegisterForm
import pl.tapo24.twa.data.register.ResponseExtractedDataFormGoogleToken
import pl.tapo24.twa.data.register.TokenPasswordData
import pl.tapo24.twa.exceptions.HttpException
import pl.tapo24.twa.exceptions.HttpMessage
import pl.tapo24.twa.exceptions.InternalException
import pl.tapo24.twa.exceptions.InternalMessage
import pl.tapo24.twa.succes.HttpSuccessMessage
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

    fun googleRegisterUser(data: RegisterForm): Result<String> {
        try {
            val response = service.googleRegisterUser(State.uid,data).execute()
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

    fun accountActivate(tokenData: AccountTokenData): Result<String> {
        try {
            val response = service.basicActivateAccountUser(State.uid, tokenData).execute()
            if (response.isSuccessful) {
                // ok
                return Result.success(HttpSuccessMessage.SuccessAccountActivate.message)
            } else {
                // not ok
                val errorMessage = response.errorBody()?.string()
                return if (response.code() == 400 && errorMessage == "You reach maximum activation account in last 30m") {
                    Result.failure(HttpException(HttpMessage.MaxActivation.message))
                } else if (response.code() == 400 && errorMessage == "Empty body!!!!") {
                    Result.failure(HttpException(HttpMessage.EmptyBody.message))
                } else if (response.code() == 400 && errorMessage == "The 2FA key is incorrectly or expired") {
                    Result.failure(HttpException(HttpMessage.TokenExpired.message))
                } else  {
                    // OTHER ERROR
                    Result.failure(HttpException(errorMessage))
                }

            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
    }

    fun checkIsAccountActivated(dbid: Dbid): Result<String> {
        try {
            val response = service.basicCheckIsAccountActivated(State.uid, dbid).execute()
            if (response.isSuccessful) {
                return Result.success(HttpSuccessMessage.UserHaveActivateAccount.message)
            }else {
                val errorMessage = response.errorBody()?.string()
                return if (errorMessage == "User haven't confirmed email") {
                    Result.failure(HttpException(HttpMessage.UserNotHaveActivateAccount.message))
                } else if (errorMessage == "User not found") {
                    Result.failure(HttpException(HttpMessage.UserNotFound.message))
                }else if (errorMessage == "Empty body!!!!") {
                    Result.failure(HttpException(HttpMessage.EmptyBody.message))
                } else  {
                    Result.failure(HttpException(errorMessage))
                }
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }

    }

    fun resendActivationToken(dbid: Dbid): Result<String> {
        try {
            val response = service.resendActivationToken(State.uid, dbid).execute()
            if (response.isSuccessful) {
                return Result.success(HttpSuccessMessage.SuccessRegenerateToken.message)
            } else {
                val errorMessage = response.errorBody()?.string()
                return if (errorMessage == "You reach maximum resend token in last 10m") {
                    Result.failure(HttpException(HttpMessage.MaxRegenerateToken.message))
                } else if (errorMessage == "User not found") {
                    Result.failure(HttpException(HttpMessage.UserNotFound.message))
                }else if (errorMessage == "Empty body!!!!") {
                    Result.failure(HttpException(HttpMessage.EmptyBody.message))
                } else  {
                    Result.failure(HttpException(errorMessage))
                }
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
    }

    fun unsubscribeAdv(tokenData: AccountTokenData): Result<String> {
        try {
            val response = service.unsubscribeAdv(State.uid, tokenData).execute()
            if (response.isSuccessful) {
                return Result.success(HttpSuccessMessage.SuccessUnSubscribeAdv.message)
            } else {
                val errorMessage = response.errorBody()?.string()
                return if (errorMessage == "Adv now is unsubscribed") {
                    Result.failure(HttpException(HttpMessage.AdvNowIsUnsubscribed.message))
                } else if (errorMessage == "Unsubscribe token is incorrectly") {
                    Result.failure(HttpException(HttpMessage.TokenIsIncorrectly.message))
                }else if (errorMessage == "User not found") {
                    Result.failure(HttpException(HttpMessage.UserNotFound.message))
                } else if (errorMessage == "Empty body!!!!") {
                    Result.failure(HttpException(HttpMessage.EmptyBody.message))
                } else  {
                    Result.failure(HttpException(errorMessage))
                }
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
    }

    fun deleteAccountFromFirstEmail(tokenData: AccountTokenData): Result<String> {
        try {
            val response = service.deleteAccountFromFirstEmail(State.uid, tokenData).execute()
            if (response.isSuccessful) {
                return Result.success(HttpSuccessMessage.SuccessDeleteAccount.message)
            } else {
                val errorMessage = response.errorBody()?.string()
                return if (errorMessage == "User already have confirmed email") {
                    Result.failure(HttpException(HttpMessage.UserHaveConfirmedEmail.message))
                } else if (errorMessage == "Delete token is incorrectly") {
                    Result.failure(HttpException(HttpMessage.TokenIsIncorrectly.message))
                }else if (errorMessage == "User not found") {
                    Result.failure(HttpException(HttpMessage.UserNotFound.message))
                } else if (errorMessage == "Empty body!!!!") {
                    Result.failure(HttpException(HttpMessage.EmptyBody.message))
                } else  {
                    Result.failure(HttpException(errorMessage))
                }
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
    }

    fun forgotPasswordStep1(data: ToLoginData): Result<String> {
        try {
            val response = service.requestForgotPassword(State.uid, data).execute()
            if (response.isSuccessful) {
                return Result.success(HttpSuccessMessage.SuccessForgotStep1.message)
            } else {
                val errorMessage = response.errorBody()?.string()
                return if (errorMessage == "User haven't confirmed email") {
                    Result.failure(HttpException(HttpMessage.UserNotHaveActivateAccount.message))
                } else if (errorMessage == "You reach maximum request forgot password attempt in last 10m") {
                    Result.failure(HttpException(HttpMessage.MaxRegenerateForgotPassword.message))
                }else if (errorMessage == "User not found") {
                    Result.failure(HttpException(HttpMessage.UserNotFound.message))
                } else if (errorMessage == "Empty body!!!!") {
                    Result.failure(HttpException(HttpMessage.EmptyBody.message))
                } else if (errorMessage == "User was banned") {
                    Result.failure(HttpException(HttpMessage.UserWasBanned.message))
                }
                else  {
                    Result.failure(HttpException(errorMessage))
                }
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
    }
    fun forgotPasswordStep2(tokenData: TokenPasswordData): Result<String> {
        try {
            val response = service.executeForgotPassword(State.uid,tokenData).execute()
            if (response.isSuccessful) {
                return Result.success(HttpSuccessMessage.SuccessForgotStep2.message)
            } else {
                val errorMessage = response.errorBody()?.string()
                return if (errorMessage == "Ip form request is different from token") {
                    Result.failure(HttpException(HttpMessage.DifferentIp.message))
                } else if (errorMessage == "You reach maximum forgot change password in last 30m") {
                    Result.failure(HttpException(HttpMessage.MaxExecuteForgotPassword.message))
                }else if (errorMessage == "Unable get token" || errorMessage == "Token was expired") {
                    Result.failure(HttpException(HttpMessage.TokenExpired.message))
                } else  {
                    Result.failure(HttpException(errorMessage))
                }
            }
        } catch (ex: Throwable) {
            return Result.failure(ex)
        }
    }

    fun getDataFromGoogleToken(googleToken: String): Result<ResponseExtractedDataFormGoogleToken> {
        return try {
            val response = service.getDataFromGoogleToken(State.uid,RequestLoginViaGoogle(googleToken)).execute()
            if (response.isSuccessful) {
                val body = response.body()
                Result.success(body!!.r)
            } else {
                val errorMessage = response.errorBody()?.string()
                Result.failure(HttpException(errorMessage))
            }
        } catch (ex: Throwable) {
            Result.failure(ex)
        }

    }

}