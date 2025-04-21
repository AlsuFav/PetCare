package ru.fav.petcare.data.remote.pojo.response
import com.google.gson.annotations.SerializedName

data class ProblemDetailResponse(
    @SerializedName("type")
    val type: String? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("status")
    val status: Int? = null,

    @SerializedName("detail")
    val detail: String? = null,

    @SerializedName("instance")
    val instance: String? = null
)