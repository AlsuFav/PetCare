package ru.fav.petcare.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.fav.petcare.domain.di.qualifier.IoDispatchers
import ru.fav.petcare.domain.model.ClientModel
import ru.fav.petcare.domain.model.JwtModel
import ru.fav.petcare.domain.repository.AuthRepository
import ru.fav.petcare.domain.repository.ClientRepository
import javax.inject.Inject

class GetClientDataUseCase @Inject constructor(
    private val clientRepository: ClientRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): ClientModel {
        return withContext(dispatcher) {
            clientRepository.getClientData()
        }
    }
}