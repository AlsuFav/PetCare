package ru.fav.petcare.data.remote.pojo.request

import com.google.gson.annotations.SerializedName

class RegisterClientRequest(
    @SerializedName("firstName")
    val firstName: String? = null,
    @SerializedName("lastName")
    val lastName: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("password")
    val password: String? = null,
    @SerializedName("confirmPassword")
    val confirmPassword: String? = null,
)
