package ru.fav.petcare.domain.provider

interface JwtProvider {
    fun getJwt(): String = ""
    fun setJwt(newApiKey: String)
}
