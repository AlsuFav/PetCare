package ru.fav.petcare.appointment.add.service.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.fav.petcare.presentation.R
import ru.fav.petcare.appointment.add.service.ui.state.SelectServiceEffect
import ru.fav.petcare.appointment.add.service.ui.state.SelectServiceEvent
import ru.fav.petcare.appointment.add.service.ui.state.SelectServiceState
import ru.fav.petcare.domain.exception.NetworkException
import ru.fav.petcare.domain.exception.NoServicesException
import ru.fav.petcare.domain.exception.ServerException
import ru.fav.petcare.domain.exception.UnauthorizedException
import ru.fav.petcare.domain.provider.ResourceProvider
import ru.fav.petcare.domain.usecase.appointment.GetAllServicesDataForPetUseCase
import ru.fav.petcare.domain.usecase.jwt.ClearJwtUseCase
import ru.fav.petcare.navigation.NavMain
import javax.inject.Inject

@HiltViewModel
class SelectServiceViewModel @Inject constructor(
    private val getAllServicesDataForPetUseCase: GetAllServicesDataForPetUseCase,
    private val clearJwtUseCase: ClearJwtUseCase,
    private val resourceProvider: ResourceProvider,
    private val navMain: NavMain,
): ViewModel() {

    private val _selectServiceState = MutableStateFlow<SelectServiceState>(SelectServiceState.Loading)
    val selectServiceState = _selectServiceState.asStateFlow()

    private val _effect = MutableSharedFlow<SelectServiceEffect>()
    val effect = _effect.asSharedFlow()

    fun reduce(event: SelectServiceEvent) {
        when (event) {
            is SelectServiceEvent.GetAllServicesForPet -> loadAllServicesForPet(event.id)
            is SelectServiceEvent.OnServiceClicked -> navigateToSelectTimeSlot(event.petId, event.serviceId)
            is SelectServiceEvent.OnBackClicked -> navigateBack()
            is SelectServiceEvent.OnCancelClicked -> navigateToAllAppointments()
        }
    }

    private fun loadAllServicesForPet(id: Long) {
        _selectServiceState.value = SelectServiceState.Loading

        viewModelScope.launch {
            runCatching {
                delay(1000)
                getAllServicesDataForPetUseCase(id)
            }.fold(
                onSuccess = {
                        services ->
                    _selectServiceState.value = SelectServiceState.Success(services)
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
            is NoServicesException -> {
                _selectServiceState.value = SelectServiceState.Error.FieldError(
                    resourceProvider.getString(R.string.no_services)
                )
                null
            }
            is NetworkException -> {
                _selectServiceState.value = SelectServiceState.Error.GlobalError
                resourceProvider.getString(R.string.error_network)
            }
            is ServerException -> {
                _selectServiceState.value = SelectServiceState.Error.GlobalError
                throwable.message ?: resourceProvider
                    .getString(R.string.error_server)
            }
            else -> {
                _selectServiceState.value = SelectServiceState.Error.GlobalError
                resourceProvider
                    .getString(R.string.error_unknown)
            }
        }
        if (message != null) {
            _effect.emit(SelectServiceEffect.ShowErrorDialog(message))
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

    private fun navigateToSelectTimeSlot(petId: Long, serviceId: Long) {
        navMain.goToSelectTimeslotPage(petId, serviceId)
    }

    override fun onCleared() {
        super.onCleared()
    }
}