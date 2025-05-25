package ru.fav.petcare.domain.usecase.pet

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.fav.petcare.domain.di.qualifier.IoDispatchers
import ru.fav.petcare.domain.repository.BreedRepository
import javax.inject.Inject

class GetAllBreedsUseCase @Inject constructor(
    private val breedRepository: BreedRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): List<String> {
        return withContext(dispatcher) {
            breedRepository.getAllBreedsContainingQuery("")
        }
    }
}