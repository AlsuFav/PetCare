package ru.fav.petcare.network.pojo.response

import com.google.gson.annotations.SerializedName

class AppointmentDataResponse (
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("petName")
    val petName: String? = null,
    @SerializedName("groomerName")
    val groomerName: String? = null,
    @SerializedName("serviceName")
    val serviceName: String? = null,
    @SerializedName("price")
    val price: Int? = null,
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("upcoming")
    val upcoming: Boolean? = null,
)