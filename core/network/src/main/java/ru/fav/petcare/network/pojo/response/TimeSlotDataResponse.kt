package ru.fav.petcare.network.pojo.response

import com.google.gson.annotations.SerializedName

class TimeSlotDataResponse (
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("groomerName")
    val groomerName: String? = null,
    @SerializedName("startTime")
    val startTime: String? = null,
    @SerializedName("endTime")
    val endTime: String? = null,
)