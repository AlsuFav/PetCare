package ru.fav.petcare.network.pojo.request

import com.google.gson.annotations.SerializedName

class UpdateClientRequest (
    @SerializedName("firstName")
    val firstName: String? = null,
    @SerializedName("lastName")
    val lastName: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
)