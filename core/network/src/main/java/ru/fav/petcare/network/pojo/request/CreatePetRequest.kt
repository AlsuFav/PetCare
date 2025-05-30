package ru.fav.petcare.network.pojo.request

import com.google.gson.annotations.SerializedName

class CreatePetRequest(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("species")
    val species: String? = null,
    @SerializedName("breed")
    val breed: String? = null,
    @SerializedName("birthDate")
    val birthDate: String? = null,
)