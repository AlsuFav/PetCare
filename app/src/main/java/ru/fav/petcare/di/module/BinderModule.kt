package ru.fav.petcare.di.module

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.fav.petcare.data.repository.AuthRepositoryImpl
import ru.fav.petcare.domain.repository.AuthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BinderModule {

    @Binds
    @Singleton
    fun bindAuthRepositoryToImpl(impl: AuthRepositoryImpl): AuthRepository

}