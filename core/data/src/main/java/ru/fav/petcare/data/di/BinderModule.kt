package ru.fav.petcare.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.fav.petcare.data.provider.AndroidResourceProvider
import ru.fav.petcare.data.provider.DateProviderImpl
import ru.fav.petcare.data.provider.JwtProviderImpl
import ru.fav.petcare.data.repository.AppointmentRepositoryImpl
import ru.fav.petcare.data.repository.AuthRepositoryImpl
import ru.fav.petcare.data.repository.BreedRepositoryImpl
import ru.fav.petcare.data.repository.ClientRepositoryImpl
import ru.fav.petcare.data.repository.JwtRepositoryImpl
import ru.fav.petcare.data.repository.PetRepositoryImpl
import ru.fav.petcare.data.repository.ServiceRepositoryImpl
import ru.fav.petcare.data.repository.TimeSlotRepositoryImpl
import ru.fav.petcare.domain.repository.BreedRepository
import ru.fav.petcare.domain.provider.DateProvider
import ru.fav.petcare.domain.provider.JwtProvider
import ru.fav.petcare.domain.provider.ResourceProvider
import ru.fav.petcare.domain.repository.AppointmentRepository
import ru.fav.petcare.domain.repository.AuthRepository
import ru.fav.petcare.domain.repository.ClientRepository
import ru.fav.petcare.domain.repository.JwtRepository
import ru.fav.petcare.domain.repository.PetRepository
import ru.fav.petcare.domain.repository.ServiceRepository
import ru.fav.petcare.domain.repository.TimeSlotRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BinderModule {

    @Binds
    @Singleton
    fun bindAuthRepositoryToImpl(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun bindClientRepositoryToImpl(impl: ClientRepositoryImpl): ClientRepository

    @Binds
    @Singleton
    fun bindPetRepositoryToImpl(impl: PetRepositoryImpl): PetRepository

    @Binds
    @Singleton
    fun bindAppointmentRepositoryToImpl(impl: AppointmentRepositoryImpl): AppointmentRepository

    @Binds
    @Singleton
    fun bindBreedRepositoryToImpl(impl: BreedRepositoryImpl): BreedRepository

    @Binds
    @Singleton
    fun bindServiceRepositoryToImpl(impl: ServiceRepositoryImpl): ServiceRepository

    @Binds
    @Singleton
    fun bindTimeSlotRepositoryToImpl(impl: TimeSlotRepositoryImpl): TimeSlotRepository

    @Binds
    @Singleton
    fun bindJwtRepositoryToImpl(impl: JwtRepositoryImpl): JwtRepository

    @Binds
    @Singleton
    fun bindResourceProviderToImpl(impl: AndroidResourceProvider): ResourceProvider

    @Binds
    @Singleton
    fun bindJwtProviderToImpl(impl: JwtProviderImpl): JwtProvider

    @Binds
    @Singleton
    fun bindDateProviderToImpl(impl: DateProviderImpl): DateProvider

}