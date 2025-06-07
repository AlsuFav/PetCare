package ru.fav.petcare.domain.model

data class FaqModel(
    val id: Long = 0,
    val question: String = "",
    val answer: String = "",
)