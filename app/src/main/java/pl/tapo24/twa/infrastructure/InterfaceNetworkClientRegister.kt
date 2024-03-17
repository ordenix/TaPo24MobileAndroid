package pl.tapo24.twa.infrastructure

import pl.tapo24.twa.data.Dbid
import pl.tapo24.twa.data.RString
import pl.tapo24.twa.data.RType
import pl.tapo24.twa.data.login.RequestLoginViaGoogle
import pl.tapo24.twa.data.login.ToLoginData
import pl.tapo24.twa.data.register.AccountTokenData
import pl.tapo24.twa.data.register.RegisterForm
import pl.tapo24.twa.data.register.ResponseExtractedDataFormGoogleToken
import pl.tapo24.twa.data.register.TokenPasswordData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface InterfaceNetworkClientRegister {
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("check_login/")
    fun checkExistLogin(@Header("INSTALLATION-UID")header: String, @Body login: RegisterForm): Call<RString>

    //Success create account
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("check_email/")
    fun checkExistEmail(@Header("INSTALLATION-UID")header: String, @Body login: RegisterForm): Call<RString>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("registerUser/")
    fun basicRegisterUser(@Header("INSTALLATION-UID")header: String, @Body login: RegisterForm): Call<RString>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("register_user_by_google_provider/")
    fun googleRegisterUser(@Header("INSTALLATION-UID")header: String, @Body login: RegisterForm): Call<RString>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("activate_account/")
    fun basicActivateAccountUser(@Header("INSTALLATION-UID")header: String, @Body tokenData: AccountTokenData): Call<RString>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("check_email_confirm/")
    fun basicCheckIsAccountActivated(@Header("INSTALLATION-UID")header: String, @Body dbid: Dbid): Call<RString>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("resend_activation_Token/")
    fun resendActivationToken(@Header("INSTALLATION-UID")header: String, @Body dbid: Dbid): Call<RString>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("unsubscribe_Adv/")
    fun unsubscribeAdv(@Header("INSTALLATION-UID")header: String, @Body tokenData: AccountTokenData): Call<RString>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("delete_account_from_first_email/")
    fun deleteAccountFromFirstEmail(@Header("INSTALLATION-UID")header: String, @Body tokenData: AccountTokenData): Call<RString>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("request_Forgot_password/")
    fun requestForgotPassword(@Header("INSTALLATION-UID")header: String, @Body data: ToLoginData): Call<RString>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("execute_forgot_password/")
    fun executeForgotPassword(@Header("INSTALLATION-UID")header: String, @Body data: TokenPasswordData): Call<RString>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("extract_data_from_google_id_token/")
    fun getDataFromGoogleToken(@Header("INSTALLATION-UID")header: String, @Body data: RequestLoginViaGoogle): Call<RType<ResponseExtractedDataFormGoogleToken>>
}