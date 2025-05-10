package ru.fav.petcare.data.provider

import ru.fav.petcare.domain.provider.JwtProvider
import javax.inject.Inject

class JwtProviderImpl @Inject constructor() : JwtProvider  {
    private var jwt: String = ""

    override fun getJwt(): String = jwt

    override fun setJwt(newApiKey: String) {
        jwt = newApiKey
    }
}
