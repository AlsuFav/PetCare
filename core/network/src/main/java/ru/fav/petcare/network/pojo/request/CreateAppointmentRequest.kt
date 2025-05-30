package ru.fav.petcare.network.pojo.request

import com.google.gson.annotations.SerializedName

class CreateAppointmentRequest(
    @SerializedName("petId")
    val petId: Long? = null,
    @SerializedName("serviceId")
    val serviceId: Long? = null,
    @SerializedName("timeSlotId")
    val timeSlotId: Long? = null,
)