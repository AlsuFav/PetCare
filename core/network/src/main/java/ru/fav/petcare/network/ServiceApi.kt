package ru.fav.petcare.network

import retrofit2.http.GET
import retrofit2.http.Path
import ru.fav.petcare.network.pojo.response.ServiceDataResponse

interface ServiceApi {
    @GET("services/for-pet/{id}")
    suspend fun getAllServicesDataForPet(
        @Path("id") id: Long,
        ): List<ServiceDataResponse>?

    @GET("services")
    suspend fun getAllServicesData(): List<ServiceDataResponse>?
}
