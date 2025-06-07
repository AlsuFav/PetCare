package ru.fav.petcare.domain.repository

import ru.fav.petcare.domain.model.ServiceModel

interface ServiceRepository {
    suspend fun getAllServicesDataForPet(id: Long) :List<ServiceModel>
    suspend fun getAllServicesData() :List<ServiceModel>
}