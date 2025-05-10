package ru.fav.petcare.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.fav.petcare.data.provider.AndroidResourceProvider
import ru.fav.petcare.data.provider.JwtProviderImpl
import ru.fav.petcare.data.repository.AuthRepositoryImpl
import ru.fav.petcare.data.repository.JwtRepositoryImpl
import ru.fav.petcare.domain.provider.JwtProvider
import ru.fav.petcare.domain.provider.ResourceProvider
import ru.fav.petcare.domain.repository.AuthRepository
import ru.fav.petcare.domain.repository.JwtRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BinderModule {

    @Binds
    @Singleton
    fun bindAuthRepositoryToImpl(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun bindResourceProviderToImpl(impl: AndroidResourceProvider): ResourceProvider

    @Binds
    @Singleton
    fun bindJwtRepositoryToImpl(impl: JwtRepositoryImpl): JwtRepository

    @Binds
    @Singleton
    fun bindJwtProviderToImpl(impl: JwtProviderImpl): JwtProvider
}