package ru.fav.petcare.network

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.fav.petcare.network.pojo.request.CreateAppointmentRequest
import ru.fav.petcare.network.pojo.response.AppointmentDataResponse

interface AppointmentApi {
    @GET("appointments/upcoming")
    suspend fun getAllUpcomingAppointmentsData(): List<AppointmentDataResponse>?

    @GET("appointments/passed")
    suspend fun getAllPassedAppointmentsData(): List<AppointmentDataResponse>?

    @GET("appointments/{id}")
    suspend fun getAppointmentData(@Path("id") id: Long) : AppointmentDataResponse?

    @DELETE("appointments/{id}")
    suspend fun cancelAppointment(@Path("id") id: Long)

    @POST("appointments")
    suspend fun createAppointment(@Body request: CreateAppointmentRequest)

    @POST("appointments/confirmation-info")
    suspend fun getAppointmentConfirmationData(@Body request: CreateAppointmentRequest): AppointmentDataResponse?
}
