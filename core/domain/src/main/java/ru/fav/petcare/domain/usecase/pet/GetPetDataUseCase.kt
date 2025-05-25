package ru.fav.petcare.domain.usecase.pet

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.fav.petcare.domain.di.qualifier.IoDispatchers
import ru.fav.petcare.domain.model.PetModel
import ru.fav.petcare.domain.repository.PetRepository
import javax.inject.Inject

class GetPetDataUseCase @Inject constructor(
    private val petRepository: PetRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(id: Long): PetModel {
        return withContext(dispatcher) {
            petRepository.getPetData(id)
        }
    }
}