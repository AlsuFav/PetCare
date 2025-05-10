package ru.fav.petcare.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.fav.petcare.data.provider.AndroidResourceProvider
import ru.fav.petcare.data.repository.AuthRepositoryImpl
import ru.fav.petcare.domain.provider.ResourceProvider
import ru.fav.petcare.domain.repository.AuthRepository
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

}