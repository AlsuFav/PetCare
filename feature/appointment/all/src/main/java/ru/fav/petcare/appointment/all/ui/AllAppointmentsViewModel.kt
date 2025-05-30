package ru.fav.petcare.appointment.all.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.fav.petcare.appointment.all.ui.state.AllAppointmentsEffect
import ru.fav.petcare.appointment.all.ui.state.AllAppointmentsEvent
import ru.fav.petcare.appointment.all.ui.state.AllAppointmentsState
import ru.fav.petcare.domain.exception.NetworkException
import ru.fav.petcare.domain.exception.ServerException
import ru.fav.petcare.domain.exception.UnauthorizedException
import ru.fav.petcare.domain.provider.ResourceProvider
import ru.fav.petcare.domain.usecase.appointment.GetAllPassedAppointmentsDataUseCase
import ru.fav.petcare.domain.usecase.appointment.GetAllUpcomingAppointmentsDataUseCase
import ru.fav.petcare.domain.usecase.jwt.ClearJwtUseCase
import ru.fav.petcare.navigation.NavMain
import ru.fav.petcare.appointment.all.R
import ru.fav.petcare.domain.exception.NoAppointmentsException
import javax.inject.Inject

@HiltViewModel
class AllAppointmentsViewModel @Inject constructor(
    private val getAllUpcomingAppointmentsDataUseCase: GetAllUpcomingAppointmentsDataUseCase,
    private val getAllPassedAppointmentsDataUseCase: GetAllPassedAppointmentsDataUseCase,
    private val clearJwtUseCase: ClearJwtUseCase,
    private val resourceProvider: ResourceProvider,
    private val navMain: NavMain,
): ViewModel() {

    private val _allAppointmentsState = MutableStateFlow<AllAppointmentsState>(AllAppointmentsState.Loading)
    val allAppointmentsState = _allAppointmentsState.asStateFlow()

    private val _effect = MutableSharedFlow<AllAppointmentsEffect>()
    val effect = _effect.asSharedFlow()

    fun reduce(event: AllAppointmentsEvent) {
        when (event) {
            is AllAppointmentsEvent.GetAllUpcomingAppointments -> loadAppointments(upcoming = true)
            is AllAppointmentsEvent.GetAllPassedAppointments -> loadAppointments(upcoming = false)
            is AllAppointmentsEvent.OnAppointmentClicked -> navigateToAppointmentDetails(event.id)
            is AllAppointmentsEvent.OnAddAppointmentClicked -> navigateToSelectPet()
        }
    }

    private fun loadAppointments(upcoming: Boolean = false) {
        _allAppointmentsState.value = AllAppointmentsState.Loading

        viewModelScope.launch {
            runCatching {
                delay(1000)
                if (upcoming) {
                    getAllUpcomingAppointmentsDataUseCase()
                } else {
                    getAllPassedAppointmentsDataUseCase()
                }
            }.fold(
                onSuccess = {
                        appointments ->
                    _allAppointmentsState.value = AllAppointmentsState.Success(appointments)
                },
                onFailure = { e ->
                    if (e is UnauthorizedException) {
                        deleteJwt()
                        navigateToAuthorization()
                    }
                    else handleError(e)
                }
            )
        }
    }

    private suspend fun handleError(throwable: Throwable) {
        val message = when (throwable) {
            is NoAppointmentsException -> {
                _allAppointmentsState.value = AllAppointmentsState.Error.FieldError(
                    resourceProvider.getString(R.string.no_appointments)
                )
                null
            }
            is NetworkException -> {
                _allAppointmentsState.value = AllAppointmentsState.Error.GlobalError
                resourceProvider.getString(ru.fav.petcare.presentation.R.string.error_network)
            }
            is ServerException -> {
                _allAppointmentsState.value = AllAppointmentsState.Error.GlobalError
                throwable.message ?: resourceProvider
                    .getString(ru.fav.petcare.presentation.R.string.error_server)
            }
            else -> {
                _allAppointmentsState.value = AllAppointmentsState.Error.GlobalError
                resourceProvider
                    .getString(ru.fav.petcare.presentation.R.string.error_unknown)
            }
        }
        if (message != null) {
            _effect.emit(AllAppointmentsEffect.ShowErrorDialog(message))
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

    private fun navigateToSelectPet() {
        navMain.goToSelectPetPage()
    }

    private fun navigateToAppointmentDetails(id: Long) {
        navMain.goToAppointmentDetailsPage(id)
    }

    override fun onCleared() {
        super.onCleared()
    }
}