package ru.fav.petcare.network.pojo.request

import com.google.gson.annotations.SerializedName

class ChangePasswordRequest (
    @SerializedName("currentPassword")
    val currentPassword: String? = null,
    @SerializedName("newPassword")
    val newPassword: String? = null,
    @SerializedName("confirmNewPassword")
    val confirmNewPassword: String? = null,
)