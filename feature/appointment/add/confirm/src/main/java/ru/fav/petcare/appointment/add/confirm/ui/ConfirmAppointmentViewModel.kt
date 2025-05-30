package ru.fav.petcare.appointment.add.confirm.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.fav.petcare.appointment.add.confirm.ui.state.AppointmentDetailsState
import ru.fav.petcare.appointment.add.confirm.ui.state.ConfirmAppointmentEffect
import ru.fav.petcare.appointment.add.confirm.ui.state.ConfirmAppointmentEvent
import ru.fav.petcare.appointment.add.confirm.ui.state.ConfirmAppointmentState
import ru.fav.petcare.domain.exception.ForbiddenAccessException
import ru.fav.petcare.domain.exception.NetworkException
import ru.fav.petcare.domain.exception.NoTimeSlotsException
import ru.fav.petcare.domain.exception.NotFoundException
import ru.fav.petcare.domain.exception.ServerException
import ru.fav.petcare.domain.exception.UnauthorizedException
import ru.fav.petcare.domain.provider.ResourceProvider
import ru.fav.petcare.domain.usecase.appointment.CreateAppointmentUseCase
import ru.fav.petcare.domain.usecase.appointment.GetAppointmentConfirmationDataUseCase
import ru.fav.petcare.domain.usecase.jwt.ClearJwtUseCase
import ru.fav.petcare.navigation.NavMain
import ru.fav.petcare.presentation.R
import javax.inject.Inject

@HiltViewModel
class ConfirmAppointmentViewModel @Inject constructor(
    private val getAppointmentConfirmationDataUseCase: GetAppointmentConfirmationDataUseCase,
    private val createAppointmentUseCase: CreateAppointmentUseCase,
    private val clearJwtUseCase: ClearJwtUseCase,
    private val resourceProvider: ResourceProvider,
    private val navMain: NavMain,
): ViewModel() {

    private val _appointmentDetailsState = MutableStateFlow<AppointmentDetailsState>(AppointmentDetailsState.Loading)
    val appointmentDetailsState = _appointmentDetailsState.asStateFlow()

    private val _confirmAppointmentState = MutableStateFlow<ConfirmAppointmentState>(
        ConfirmAppointmentState.Initial)
    val confirmAppointmentState = _confirmAppointmentState.asStateFlow()

    private val _effect = MutableSharedFlow<ConfirmAppointmentEffect>()
    val effect = _effect.asSharedFlow()

    fun reduce(event: ConfirmAppointmentEvent) {
        when (event) {
            is ConfirmAppointmentEvent.GetConfirmAppointmentData -> loadAppointmentData(
                petId = event.petId,
                serviceId = event.serviceId,
                timeSlotId = event.timeSlotId
            )
            is ConfirmAppointmentEvent.OnConfirmClicked -> createAppointment(
                petId = event.petId,
                serviceId = event.serviceId,
                timeSlotId = event.timeSlotId
            )
            is ConfirmAppointmentEvent.OnBackClicked -> navigateBack()
            is ConfirmAppointmentEvent.OnCancelClicked -> navigateToAllAppointments()
        }
    }

    private fun loadAppointmentData(
        petId: Long,
        serviceId: Long,
        timeSlotId: Long
    ) {
        _appointmentDetailsState.value = AppointmentDetailsState.Loading
        viewModelScope.launch {
            runCatching {
                getAppointmentConfirmationDataUseCase(
                    petId = petId,
                    serviceId = serviceId,
                    timeSlotId = timeSlotId
                )
            }.fold(
                onSuccess = {
                        appointment ->
                    _appointmentDetailsState.value = AppointmentDetailsState.Success(appointment)
                },
                onFailure = { e ->
                    if (e is UnauthorizedException) {
                        deleteJwt()
                        navigateToAuthorization()
                    } else if (e is NotFoundException || e is ForbiddenAccessException) {
                        navigateToAllAppointments()
                    }
                    else {
                        handleError(e)
                        _appointmentDetailsState.value = AppointmentDetailsState.Error
                    }
                }
            )
        }
    }

    private fun createAppointment(
        petId: Long,
        serviceId: Long,
        timeSlotId: Long
    ) {
        _confirmAppointmentState.value = ConfirmAppointmentState.Loading

        viewModelScope.launch {
            runCatching {
                createAppointmentUseCase(
                    petId = petId,
                    serviceId = serviceId,
                    timeSlotId = timeSlotId
                )
            }.fold(
                onSuccess = {
                    _effect.emit(
                        ConfirmAppointmentEffect.ShowToast(
                            resourceProvider.getString(
                                ru.fav.petcare.appointment.add.confirm.R.string.appointment_created_successfully
                            )
                        )
                    )
                    _confirmAppointmentState.value = ConfirmAppointmentState.Success
                    navigateToAllAppointments()
                },
                onFailure = { e ->
                    if (e is UnauthorizedException) {
                        deleteJwt()
                        navigateToAuthorization()
                    } else if (e is NotFoundException || e is ForbiddenAccessException) {
                        navigateToAllAppointments()
                    }
                    else {
                        handleError(e)
                        _confirmAppointmentState.value = ConfirmAppointmentState.Error
                    }
                }
            )
        }
    }

    private suspend fun handleError(throwable: Throwable) {
        val message =  when (throwable) {
            is NetworkException ->
                resourceProvider.getString(R.string.error_network)
            is ServerException ->
                throwable.message ?: resourceProvider.getString(R.string.error_server)
            else ->
                resourceProvider.getString(R.string.error_unknown)
        }

        _effect.emit(ConfirmAppointmentEffect.ShowErrorDialog(message))
    }

    private fun deleteJwt() {
        viewModelScope.launch {
            runCatching {
                clearJwtUseCase()
            }.fold(
                onSuccess = {
                    navigateToAuthorization()
                },
                onFailure = {
                    navigateToAuthorization()
                }
            )
        }
    }

    private fun navigateToAuthorization() {
        navMain.goToAuthorizationPage()
    }

    private fun navigateToAllAppointments() {
        navMain.goToAllAppointmentsPage()
    }

    private fun navigateBack() {
        navMain.goBack()
    }

    override fun onCleared() {
        super.onCleared()
    }
}