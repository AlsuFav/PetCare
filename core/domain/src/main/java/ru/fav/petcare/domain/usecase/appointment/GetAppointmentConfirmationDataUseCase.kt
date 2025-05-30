package ru.fav.petcare.domain.usecase.appointment

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.fav.petcare.domain.di.qualifier.IoDispatchers
import ru.fav.petcare.domain.model.AppointmentModel
import ru.fav.petcare.domain.repository.AppointmentRepository
import javax.inject.Inject

class GetAppointmentConfirmationDataUseCase @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        petId: Long,
        serviceId: Long,
        timeSlotId: Long,
    ) : AppointmentModel {
        return withContext(dispatcher) {
            appointmentRepository.getAppointmentConfirmationData(
                petId = petId,
                serviceId = serviceId,
                timeSlotId = timeSlotId
            )
        }
    }
}