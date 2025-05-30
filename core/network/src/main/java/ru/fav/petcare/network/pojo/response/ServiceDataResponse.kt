package ru.fav.petcare.network.pojo.response

import com.google.gson.annotations.SerializedName

class ServiceDataResponse (
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("price")
    val price: Int? = null,
)