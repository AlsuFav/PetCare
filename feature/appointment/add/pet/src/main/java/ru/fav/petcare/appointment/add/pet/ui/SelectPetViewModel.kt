package ru.fav.petcare.appointment.add.pet.ui

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
import ru.fav.petcare.appointment.add.pet.ui.state.SelectPetEffect
import ru.fav.petcare.appointment.add.pet.ui.state.SelectPetEvent
import ru.fav.petcare.appointment.add.pet.ui.state.SelectPetState
import ru.fav.petcare.domain.exception.NetworkException
import ru.fav.petcare.domain.exception.NoPetsException
import ru.fav.petcare.domain.exception.ServerException
import ru.fav.petcare.domain.exception.UnauthorizedException
import ru.fav.petcare.domain.provider.ResourceProvider
import ru.fav.petcare.domain.usecase.jwt.ClearJwtUseCase
import ru.fav.petcare.domain.usecase.pet.GetAllPetsDataUseCase
import ru.fav.petcare.navigation.NavMain
import javax.inject.Inject

@HiltViewModel
class SelectPetViewModel @Inject constructor(
    private val getAllPetsDataUseCase: GetAllPetsDataUseCase,
    private val clearJwtUseCase: ClearJwtUseCase,
    private val resourceProvider: ResourceProvider,
    private val navMain: NavMain,
): ViewModel() {

    private val _selectPetState = MutableStateFlow<SelectPetState>(SelectPetState.Loading)
    val selectPetState = _selectPetState.asStateFlow()

    private val _effect = MutableSharedFlow<SelectPetEffect>()
    val effect = _effect.asSharedFlow()

    fun reduce(event: SelectPetEvent) {
        when (event) {
            is SelectPetEvent.GetAllPets -> loadAllPets()
            is SelectPetEvent.OnPetClicked -> navigateToSelectService(event.id)
            is SelectPetEvent.OnAddPetClicked -> navigateToAddPet()
            is SelectPetEvent.OnCancelClicked -> navigateToAllAppointments()
        }
    }

    private fun loadAllPets() {
        _selectPetState.value = SelectPetState.Loading

        viewModelScope.launch {
            runCatching {
                delay(1000)
                getAllPetsDataUseCase()
            }.fold(
                onSuccess = {
                        pets ->
                    _selectPetState.value = SelectPetState.Success(pets)
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
            is NoPetsException -> {
                _selectPetState.value = SelectPetState.Error.FieldError(
                    resourceProvider.getString(R.string.no_pets)
                )
                null
            }
            is NetworkException -> {
                _selectPetState.value = SelectPetState.Error.GlobalError
                resourceProvider.getString(R.string.error_network)
            }
            is ServerException -> {
                _selectPetState.value = SelectPetState.Error.GlobalError
                throwable.message ?: resourceProvider
                    .getString(R.string.error_server)
            }
            else -> {
                _selectPetState.value = SelectPetState.Error.GlobalError
                resourceProvider
                    .getString(R.string.error_unknown)
            }
        }
        if (message != null) {
            _effect.emit(SelectPetEffect.ShowErrorDialog(message))
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

    private fun navigateToAddPet() {
        navMain.goToAddPetPage()
    }

    private fun navigateToSelectService(id: Long) {
        navMain.goToSelectServicePage(id)
    }

    override fun onCleared() {
        super.onCleared()
    }
}