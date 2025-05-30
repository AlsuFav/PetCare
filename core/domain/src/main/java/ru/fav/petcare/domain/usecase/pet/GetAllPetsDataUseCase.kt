package ru.fav.petcare.domain.usecase.pet

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.fav.petcare.domain.di.qualifier.IoDispatchers
import ru.fav.petcare.domain.exception.NoPetsException
import ru.fav.petcare.domain.model.PetModel
import ru.fav.petcare.domain.repository.PetRepository
import javax.inject.Inject

class GetAllPetsDataUseCase @Inject constructor(
    private val petRepository: PetRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): List<PetModel> {
        return withContext(dispatcher) {
            val pets = petRepository.getAllPetsData()

            if (pets.isEmpty()) {
                throw NoPetsException(null)
            } else {
                pets
            }
        }
    }
}