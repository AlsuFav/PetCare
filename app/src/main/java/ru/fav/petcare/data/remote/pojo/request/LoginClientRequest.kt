package ru.fav.petcare.data.remote.pojo.request

import com.google.gson.annotations.SerializedName

class LoginClientRequest(
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("password")
    val password: String? = null,
)