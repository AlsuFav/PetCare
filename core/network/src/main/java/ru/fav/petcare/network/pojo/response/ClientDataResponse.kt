package ru.fav.petcare.network.pojo.response

import com.google.gson.annotations.SerializedName

class ClientDataResponse (
    @SerializedName("firstName")
    val firstName: String? = null,
    @SerializedName("lastName")
    val lastName: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
)