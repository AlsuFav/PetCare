package ru.fav.petcare.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.fav.petcare.domain.di.qualifier.IoDispatchers
import ru.fav.petcare.domain.repository.ClientRepository
import javax.inject.Inject

class UpdateClientDataUseCase @Inject constructor(
    private val clientRepository: ClientRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        phone: String
    ) {
        withContext(dispatcher) {
            clientRepository.updateClientData(
                firstName = firstName,
                lastName = lastName,
                phone = phone
            )
        }
    }
}