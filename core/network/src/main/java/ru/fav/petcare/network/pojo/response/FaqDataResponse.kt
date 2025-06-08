package ru.fav.petcare.network.pojo.response

import com.google.gson.annotations.SerializedName

class FaqDataResponse (
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("question")
    val question: String? = null,
    @SerializedName("answer")
    val answer: String? = null,
)