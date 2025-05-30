package ru.fav.petcare.pet.all.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.fav.petcare.domain.exception.NetworkException
import ru.fav.petcare.domain.exception.NoPetsException
import ru.fav.petcare.domain.exception.ServerException
import ru.fav.petcare.domain.exception.UnauthorizedException
import ru.fav.petcare.domain.provider.ResourceProvider
import ru.fav.petcare.domain.usecase.jwt.ClearJwtUseCase
import ru.fav.petcare.domain.usecase.pet.GetAllPetsDataUseCase
import ru.fav.petcare.navigation.NavMain
import ru.fav.petcare.pet.all.ui.state.AllPetsEffect
import ru.fav.petcare.pet.all.ui.state.AllPetsEvent
import ru.fav.petcare.pet.all.ui.state.AllPetsState
import ru.fav.petcare.presentation.R
import javax.inject.Inject

@HiltViewModel
class AllPetsViewModel @Inject constructor(
    private val getAllPetsDataUseCase: GetAllPetsDataUseCase,
    private val clearJwtUseCase: ClearJwtUseCase,
    private val resourceProvider: ResourceProvider,
    private val navMain: NavMain,
): ViewModel() {

    private val _allPetsState = MutableStateFlow<AllPetsState>(AllPetsState.Loading)
    val allPetsState = _allPetsState.asStateFlow()

    private val _effect = MutableSharedFlow<AllPetsEffect>()
    val effect = _effect.asSharedFlow()

    fun reduce(event: AllPetsEvent) {
        when (event) {
            is AllPetsEvent.GetAllPets -> loadAllPets()
            is AllPetsEvent.OnPetClicked -> navigateToPetDetails(event.id)
            is AllPetsEvent.OnAddPetClicked -> navigateToAddPet()
        }
    }

    private fun loadAllPets() {
        _allPetsState.value = AllPetsState.Loading

        viewModelScope.launch {
            runCatching {
//                delay(2000)
                getAllPetsDataUseCase()
            }.fold(
                onSuccess = {
                    pets ->
                        _allPetsState.value = AllPetsState.Success(pets)
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
                _allPetsState.value = AllPetsState.Error.FieldError(
                    resourceProvider.getString(R.string.no_pets)
                )
                null
            }
            is NetworkException -> {
                _allPetsState.value = AllPetsState.Error.GlobalError
                resourceProvider.getString(R.string.error_network)
            }
            is ServerException -> {
                _allPetsState.value = AllPetsState.Error.GlobalError
                throwable.message ?: resourceProvider
                    .getString(R.string.error_server)
            }
            else -> {
                _allPetsState.value = AllPetsState.Error.GlobalError
                resourceProvider
                    .getString(R.string.error_unknown)
            }
        }
        if (message != null) {
            _effect.emit(AllPetsEffect.ShowErrorDialog(message))
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

    private fun navigateToAddPet() {
        navMain.goToAddPetPage()
    }

    private fun navigateToPetDetails(id: Long) {
        navMain.goToPetDetailsPage(id)
    }

    override fun onCleared() {
        super.onCleared()
    }
}