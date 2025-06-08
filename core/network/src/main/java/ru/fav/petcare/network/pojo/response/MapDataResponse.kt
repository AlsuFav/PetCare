package ru.fav.petcare.network.pojo.response

import com.google.gson.annotations.SerializedName

class MapDataResponse (
    @SerializedName("latitude")
    val latitude: Double? = null,
    @SerializedName("longitude")
    val longitude: Double? = null,
    @SerializedName("address")
    val address: String? = null,
)