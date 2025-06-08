package ru.fav.petcare.domain.usecase.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.fav.petcare.domain.di.qualifier.IoDispatchers
import ru.fav.petcare.domain.model.FaqModel
import ru.fav.petcare.domain.repository.CommonRepository
import javax.inject.Inject

class GetFaqDataUseCase @Inject constructor(
    private val commonRepository: CommonRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): List<FaqModel> {
        return withContext(dispatcher) {
            commonRepository.getFaqData()
        }
    }
}