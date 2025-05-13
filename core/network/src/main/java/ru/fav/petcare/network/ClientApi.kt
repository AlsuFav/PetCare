package ru.fav.petcare.network

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import ru.fav.petcare.network.pojo.request.ChangePasswordRequest
import ru.fav.petcare.network.pojo.request.UpdateClientRequest
import ru.fav.petcare.network.pojo.response.ClientDataResponse

interface ClientApi {
    @GET("client/me")
    suspend fun getClientData(): ClientDataResponse?

    @PATCH("client/me/password")
    suspend fun changePassword(@Body request: ChangePasswordRequest)

    @PUT("client/me")
    suspend fun updateClientData(@Body updateClientRequest: UpdateClientRequest)

    @DELETE("client/me")
    suspend fun deleteClient()
}
