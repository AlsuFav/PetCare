package ru.fav.petcare.network

import retrofit2.http.GET
import ru.fav.petcare.network.pojo.response.TimeSlotDataResponse

interface TimeSlotApi {
    @GET("timeslots")
    suspend fun getAllTimeSlotsData(): List<TimeSlotDataResponse>?
}
