package ru.fav.petcare.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.fav.petcare.network.pojo.response.BreedResponse

interface BreedApi {
    @GET("breeds")
    suspend fun getAllBreedsContainingQuery(
        @Query("query") query: String,
        ): List<BreedResponse>?
}