package ru.fav.petcare.appointment.add.timeslot.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.fav.petcare.appointment.add.timeslot.ui.state.SelectTimeSlotEffect
import ru.fav.petcare.appointment.add.timeslot.ui.state.SelectTimeSlotEvent
import ru.fav.petcare.appointment.add.timeslot.ui.state.SelectTimeSlotState
import ru.fav.petcare.domain.exception.NetworkException
import ru.fav.petcare.domain.exception.NoTimeSlotsException
import ru.fav.petcare.domain.exception.ServerException
import ru.fav.petcare.domain.exception.UnauthorizedException
import ru.fav.petcare.domain.provider.ResourceProvider
import ru.fav.petcare.domain.usecase.appointment.GetAllTimeSlotsDataUseCase
import ru.fav.petcare.domain.usecase.jwt.ClearJwtUseCase
import ru.fav.petcare.navigation.NavMain
import ru.fav.petcare.presentation.R
import javax.inject.Inject

@HiltViewModel
class SelectTimeSlotViewModel @Inject constructor(
    private val getAllTimeSlotsDataUseCase: GetAllTimeSlotsDataUseCase,
    private val clearJwtUseCase: ClearJwtUseCase,
    private val resourceProvider: ResourceProvider,
    private val navMain: NavMain,
): ViewModel() {

    private val _selectTimeSlotState = MutableStateFlow<SelectTimeSlotState>(SelectTimeSlotState.Loading)
    val selectTimeSlotState = _selectTimeSlotState.asStateFlow()

    private val _effect = MutableSharedFlow<SelectTimeSlotEffect>()
    val effect = _effect.asSharedFlow()

    fun reduce(event: SelectTimeSlotEvent) {
        when (event) {
            is SelectTimeSlotEvent.GetAllTimeSlots -> loadAllTimeSlots()
            is SelectTimeSlotEvent.OnTimeSlotClicked -> navigateToConfirmAppointment(
                petId = event.petId,
                serviceId = event.serviceId,
                timeSlotId = event.timeSlotId
            )
            is SelectTimeSlotEvent.OnBackClicked -> navigateBack()
            is SelectTimeSlotEvent.OnCancelClicked -> navigateToAllAppointments()
        }
    }

    private fun loadAllTimeSlots() {
        _selectTimeSlotState.value = SelectTimeSlotState.Loading

        viewModelScope.launch {
            runCatching {
                delay(1000)
                getAllTimeSlotsDataUseCase()
            }.fold(
                onSuccess = {
                        timeSlots ->
                    _selectTimeSlotState.value = SelectTimeSlotState.Success(timeSlots)
                },
                onFailure = { e ->
                    if (e is UnauthorizedException) {
                        deleteJwt()
                        navigateToAuthorization()
                    } else handleError(e)
                }
            )
        }
    }

    private suspend fun handleError(throwable: Throwable) {
        val message = when (throwable) {
            is NoTimeSlotsException -> {
                _selectTimeSlotState.value = SelectTimeSlotState.Error.FieldError(
                    resourceProvider.getString(ru.fav.petcare.appointment.add.timeslot.R.string.no_timeslots)
                )
                null
            }
            is NetworkException -> {
                _selectTimeSlotState.value = SelectTimeSlotState.Error.GlobalError
                resourceProvider.getString(R.string.error_network)
            }
            is ServerException -> {
                _selectTimeSlotState.value = SelectTimeSlotState.Error.GlobalError
                throwable.message ?: resourceProvider
                    .getString(R.string.error_server)
            }
            else -> {
                _selectTimeSlotState.value = SelectTimeSlotState.Error.GlobalError
                resourceProvider
                    .getString(R.string.error_unknown)
            }
        }
        if (message != null) {
            _effect.emit(SelectTimeSlotEffect.ShowErrorDialog(message))
        }
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

    private fun navigateToConfirmAppointment(
        petId: Long,
        serviceId: Long,
        timeSlotId: Long
    ) {
        navMain.goToConfirmAppointmentPage(
            petId = petId,
            serviceId = serviceId,
            timeSlotId = timeSlotId
        )
    }

    override fun onCleared() {
        super.onCleared()
    }
}