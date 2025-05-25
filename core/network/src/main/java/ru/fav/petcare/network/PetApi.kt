package ru.fav.petcare.network

import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import ru.fav.petcare.network.pojo.request.CreatePetRequest
import ru.fav.petcare.network.pojo.request.UpdatePetRequest
import ru.fav.petcare.network.pojo.response.PetDataResponse

interface PetApi {
    @GET("pets")
    suspend fun getAllPetsData(): List<PetDataResponse>?

    @GET("pets/{id}")
    suspend fun getPetData(@Path("id") id: Long) : PetDataResponse

    @POST("pets")
    suspend fun createPet(@Body request: CreatePetRequest)

    @PUT("pets/{id}")
    suspend fun updatePet(
        @Path("id") id: Long,
        @Body request: UpdatePetRequest
    )

    @DELETE("pets/{id}")
    suspend fun deletePet(@Path("id") id: Long)
}
