package ru.fav.petcare.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.fav.petcare.network.AppointmentApi
import ru.fav.petcare.network.BuildConfig.PETCARE_BASE_URL
import ru.fav.petcare.network.AuthApi
import ru.fav.petcare.network.BreedApi
import ru.fav.petcare.network.ClientApi
import ru.fav.petcare.network.CommonApi
import ru.fav.petcare.network.PetApi
import ru.fav.petcare.network.ServiceApi
import ru.fav.petcare.network.TimeSlotApi
import ru.fav.petcare.network.interceptor.JwtInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideOkHttpClient(
        jwtInterceptor: JwtInterceptor
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(jwtInterceptor)
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

    @Provides
    @Singleton
    fun provideClientApi(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
    ): ClientApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(PETCARE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()

        return retrofit.create(ClientApi::class.java)
    }

    @Provides
    @Singleton
    fun providePetApi(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
    ): PetApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(PETCARE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()

        return retrofit.create(PetApi::class.java)
    }


    @Provides
    @Singleton
    fun provideBreedApi(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
    ): BreedApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(PETCARE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()

        return retrofit.create(BreedApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAppointmentApi(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
    ): AppointmentApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(PETCARE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()

        return retrofit.create(AppointmentApi::class.java)
    }

    @Provides
    @Singleton
    fun provideServiceApi(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
    ): ServiceApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(PETCARE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()

        return retrofit.create(ServiceApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTimeSlotApi(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
    ): TimeSlotApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(PETCARE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()

        return retrofit.create(TimeSlotApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCommonApi(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
    ): CommonApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(PETCARE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()

        return retrofit.create(CommonApi::class.java)
    }
}