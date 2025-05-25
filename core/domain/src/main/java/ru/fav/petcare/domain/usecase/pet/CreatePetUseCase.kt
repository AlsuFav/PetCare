package ru.fav.petcare.domain.usecase.pet

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.fav.petcare.domain.di.qualifier.IoDispatchers
import ru.fav.petcare.domain.repository.PetRepository
import javax.inject.Inject

class CreatePetUseCase @Inject constructor(
    private val petRepository: PetRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        name: String,
        species: String,
        breed: String?,
        birthDate: String,
    ) {
        return withContext(dispatcher) {
            petRepository.createPet(
                name = name,
                species = species,
                breed = breed,
                birthDate = birthDate
            )
        }
    }
}
