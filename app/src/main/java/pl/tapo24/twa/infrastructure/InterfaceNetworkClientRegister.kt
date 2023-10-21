package pl.tapo24.twa.infrastructure

import pl.tapo24.twa.data.RString
import pl.tapo24.twa.data.register.RegisterForm
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
}