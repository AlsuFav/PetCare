package ru.fav.petcare.data.remote.pojo.response

import com.google.gson.annotations.SerializedName

class JwtResponse(
    @SerializedName("token")
    val token: String? = null
)