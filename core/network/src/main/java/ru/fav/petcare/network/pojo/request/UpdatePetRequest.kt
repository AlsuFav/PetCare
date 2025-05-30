package ru.fav.petcare.network.pojo.request

import com.google.gson.annotations.SerializedName

class UpdatePetRequest(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("birthDate")
    val birthDate: String? = null,
)