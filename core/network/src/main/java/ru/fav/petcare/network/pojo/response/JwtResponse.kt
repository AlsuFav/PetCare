package ru.fav.petcare.network.pojo.response

import com.google.gson.annotations.SerializedName

class JwtResponse(
    @SerializedName("token")
    val token: String? = null
)