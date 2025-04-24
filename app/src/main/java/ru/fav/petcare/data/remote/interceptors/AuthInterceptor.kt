//package ru.fav.petcare.data.remote.interceptors
//
//import okhttp3.Interceptor
//import okhttp3.Response
//import javax.inject.Inject
//
//class AuthInterceptor @Inject constructor(
//    private val token: String
//) : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val request = chain.request().newBuilder()
//            .addHeader("Authorization", "Bearer $token")
//            .build()
//        return chain.proceed(request)
//    }
//}