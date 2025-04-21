package ru.fav.petcare.domain.models

data class ProblemDetailsModel(
    val type: String = "",
    val title: String = "",
    val status: Int = -1,
    val detail: String = "",
    val instance: String = ""
)
