package ru.fav.petcare.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import ru.fav.petcare.domain.provider.JwtProvider
import javax.inject.Inject

class JwtInterceptor @Inject constructor(
    private val jwtProvider: JwtProvider
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val jwt = jwtProvider.getJwt()
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $jwt")
            .build()
        return chain.proceed(request)
    }
}