package ru.fav.petcare.appointment.details.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.fav.petcare.appointment.details.ui.state.AppointmentDetailsEffect
import ru.fav.petcare.appointment.details.ui.state.AppointmentDetailsEvent
import ru.fav.petcare.appointment.details.ui.state.AppointmentDetailsState
import ru.fav.petcare.appointment.details.ui.state.CancelAppointmentState
import ru.fav.petcare.domain.exception.ForbiddenAccessException
import ru.fav.petcare.domain.exception.NetworkException
import ru.fav.petcare.domain.exception.NotFoundException
import ru.fav.petcare.domain.exception.ServerException
import ru.fav.petcare.domain.exception.UnauthorizedException
import ru.fav.petcare.domain.provider.ResourceProvider
import ru.fav.petcare.domain.usecase.appointment.CancelAppointmentUseCase
import ru.fav.petcare.domain.usecase.appointment.GetAppointmentDataUseCase
import ru.fav.petcare.domain.usecase.jwt.ClearJwtUseCase
import ru.fav.petcare.navigation.NavMain
import ru.fav.petcare.presentation.R
import javax.inject.Inject

@HiltViewModel
class AppointmentDetailsViewModel @Inject constructor(
    private val getAppointmentDataUseCase: GetAppointmentDataUseCase,
    private val cancelAppointmentUseCase: CancelAppointmentUseCase,
    private val clearJwtUseCase: ClearJwtUseCase,
    private val resourceProvider: ResourceProvider,
    private val navMain: NavMain,
) : ViewModel() {

    private val _appointmentDetailsState = MutableStateFlow<AppointmentDetailsState>(
        AppointmentDetailsState.Loading)
    val appointmentDetailsState = _appointmentDetailsState.asStateFlow()

    private val _cancelAppointmentState = MutableStateFlow<CancelAppointmentState>(
        CancelAppointmentState.Initial)
    val cancelAppointmentState = _cancelAppointmentState.asStateFlow()

    private val _effect = MutableSharedFlow<AppointmentDetailsEffect>()
    val effect = _effect.asSharedFlow()

    fun reduce(event: AppointmentDetailsEvent) {
        viewModelScope.launch {
            when (event) {
                is AppointmentDetailsEvent.GetAppointmentData -> loadAppointmentData(event.id)
                is AppointmentDetailsEvent.OnBackClicked -> navigateBack()
                is AppointmentDetailsEvent.OnCancelAppointmentClicked -> {
                    _effect.emit(
                        AppointmentDetailsEffect.ShowCancelAppointmentConfirmation(
                            resourceProvider.getString(
                                ru.fav.petcare.appointment.details.R.string.confirm_cancel_appointment_message
                            )
                        )
                    )
                }
                is AppointmentDetailsEvent.OnConfirmCancelAppointmentClicked -> cancelAppointment(event.id)
            }
        }
    }

    private fun loadAppointmentData(id: Long) {
        _appointmentDetailsState.value = AppointmentDetailsState.Loading
        viewModelScope.launch {
            runCatching {
                getAppointmentDataUseCase(id)
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

    private fun cancelAppointment(id: Long) {
        _cancelAppointmentState.value = CancelAppointmentState.Loading

        viewModelScope.launch {
            runCatching {
                cancelAppointmentUseCase(id)
            }.fold(
                onSuccess = {
                    _effect.emit(
                        AppointmentDetailsEffect.ShowToast(
                            resourceProvider.getString(
                                ru.fav.petcare.appointment.details.R.string.appointment_cancelled_successfully
                            )
                        )
                    )
                    _cancelAppointmentState.value = CancelAppointmentState.Success
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
                        _cancelAppointmentState.value = CancelAppointmentState.Error
                    }
                }
            )
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

    private suspend fun handleError(throwable: Throwable) {
        val message =  when (throwable) {
            is NetworkException ->
                resourceProvider.getString(R.string.error_network)
            is ServerException ->
                throwable.message ?: resourceProvider.getString(R.string.error_server)
            else ->
                resourceProvider.getString(R.string.error_unknown)
        }

        _effect.emit(AppointmentDetailsEffect.ShowErrorDialog(message))
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
