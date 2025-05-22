package ru.fav.petcare.network

import retrofit2.http.Body
import retrofit2.http.POST
import ru.fav.petcare.network.pojo.request.LoginClientRequest
import ru.fav.petcare.network.pojo.request.RegisterClientRequest
import ru.fav.petcare.network.pojo.response.JwtResponse

interface AuthApi {
    @POST("login")
    suspend fun login(@Body loginClientRequest: LoginClientRequest): JwtResponse?

    @POST("register")
    suspend fun register(@Body registerClientRequest: RegisterClientRequest): JwtResponse?
}
