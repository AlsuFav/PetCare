package ru.fav.petcare.network.pojo.response

import com.google.gson.annotations.SerializedName

class PetDataResponse (
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("species")
    val species: String? = null,
    @SerializedName("breed")
    val breed: String? = null,
    @SerializedName("birthDate")
    val birthDate: String? = null,
)