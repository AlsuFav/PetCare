package ru.fav.petcare.domain.provider

interface JwtProvider {
    fun getApiKey(): String = ""
    fun setApiKey(newApiKey: String)
}
