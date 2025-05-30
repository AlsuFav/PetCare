package ru.fav.petcare.domain.usecase.appointment

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.fav.petcare.domain.di.qualifier.IoDispatchers
import ru.fav.petcare.domain.exception.NoTimeSlotsException
import ru.fav.petcare.domain.model.TimeSlotModel
import ru.fav.petcare.domain.provider.DateProvider
import ru.fav.petcare.domain.repository.TimeSlotRepository
import javax.inject.Inject

class GetAllTimeSlotsDataUseCase @Inject constructor(
    private val timeSlotRepository: TimeSlotRepository,
    private val dateProvider: DateProvider,
    @IoDispatchers private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): List<Pair<String, List<TimeSlotModel>>> {
        return withContext(dispatcher) {
            timeSlotRepository.getAllTimeSlotsData()
                        .takeIf { it.isNotEmpty() }?.sortedWith(compareBy<TimeSlotModel> { slot ->
                    dateProvider.parseDate(slot.date).timeInMillis
                }.thenBy { slot ->
                    slot.time
                })?.groupBy { it.date }?.toList()
                ?: throw NoTimeSlotsException(null)
        }
    }
}