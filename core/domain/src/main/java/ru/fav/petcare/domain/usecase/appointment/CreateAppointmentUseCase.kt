package ru.fav.petcare.domain.usecase.appointment

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.fav.petcare.domain.di.qualifier.IoDispatchers
import ru.fav.petcare.domain.repository.AppointmentRepository
import javax.inject.Inject

class CreateAppointmentUseCase @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        petId: Long,
        serviceId: Long,
        timeSlotId: Long,
    ) {
        return withContext(dispatcher) {
            appointmentRepository.createAppointment(
                petId = petId,
                serviceId = serviceId,
                timeSlotId = timeSlotId
            )
        }
    }
}
