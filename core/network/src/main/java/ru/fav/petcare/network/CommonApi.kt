package ru.fav.petcare.network

import retrofit2.http.GET
import ru.fav.petcare.network.pojo.response.FaqDataResponse
import ru.fav.petcare.network.pojo.response.MapDataResponse

interface CommonApi {
    @GET("map")
    suspend fun getMapData(): MapDataResponse?

    @GET("faq")
    suspend fun getFaqData(): List<FaqDataResponse>?
}
