package ru.fav.petcare.domain.usecase.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.fav.petcare.domain.di.qualifier.IoDispatchers
import ru.fav.petcare.domain.model.MapModel
import ru.fav.petcare.domain.repository.CommonRepository
import javax.inject.Inject

class GetMapDataUseCase @Inject constructor(
    private val commonRepository: CommonRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): MapModel {
        return withContext(dispatcher) {
            commonRepository.getMapData()
        }
    }
}