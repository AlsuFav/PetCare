package ru.fav.petcare.di.module

import android.annotation.SuppressLint
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.fav.petcare.BuildConfig.PETCARE_BASE_URL
import ru.fav.petcare.data.remote.AuthApi
import java.security.SecureRandom
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.cert.X509Certificate
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideAuthApi(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
    ): AuthApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(PETCARE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()

        return retrofit.create(AuthApi::class.java)
    }
}