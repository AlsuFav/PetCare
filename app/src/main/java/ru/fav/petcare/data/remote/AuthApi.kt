package ru.fav.petcare.data.remote

import retrofit2.http.Body
import retrofit2.http.POST
import ru.fav.petcare.data.remote.pojo.request.LoginClientRequest
import ru.fav.petcare.data.remote.pojo.response.JwtResponse

interface AuthApi {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginClientRequest): JwtResponse?
}
