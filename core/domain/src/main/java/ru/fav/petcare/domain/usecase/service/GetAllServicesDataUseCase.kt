package ru.fav.petcare.domain.usecase.service

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.fav.petcare.domain.di.qualifier.IoDispatchers
import ru.fav.petcare.domain.exception.NoServicesException
import ru.fav.petcare.domain.model.ServiceModel
import ru.fav.petcare.domain.repository.ServiceRepository
import javax.inject.Inject

class GetAllServicesDataUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): List<ServiceModel> {
        return withContext(dispatcher) {
            val services = serviceRepository.getAllServicesData()

            if (services.isEmpty()) {
                throw NoServicesException(null)
            } else {
                services
            }
        }
    }
}