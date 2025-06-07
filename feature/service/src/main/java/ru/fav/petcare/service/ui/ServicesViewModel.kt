package ru.fav.petcare.service.ui

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
import ru.fav.petcare.domain.exception.NetworkException
import ru.fav.petcare.domain.exception.NoServicesException
import ru.fav.petcare.domain.exception.ServerException
import ru.fav.petcare.domain.exception.UnauthorizedException
import ru.fav.petcare.domain.provider.ResourceProvider
import ru.fav.petcare.domain.usecase.service.GetAllServicesDataUseCase
import ru.fav.petcare.domain.usecase.jwt.ClearJwtUseCase
import ru.fav.petcare.navigation.NavMain
import ru.fav.petcare.service.ui.state.ServicesEffect
import ru.fav.petcare.service.ui.state.ServicesEvent
import ru.fav.petcare.service.ui.state.ServicesState
import javax.inject.Inject

@HiltViewModel
class ServicesViewModel @Inject constructor(
    private val getAllServicesDataUseCase: GetAllServicesDataUseCase,
    private val clearJwtUseCase: ClearJwtUseCase,
    private val resourceProvider: ResourceProvider,
    private val navMain: NavMain,
): ViewModel() {

    private val _servicesState = MutableStateFlow<ServicesState>(ServicesState.Loading)
    val servicesState = _servicesState.asStateFlow()

    private val _effect = MutableSharedFlow<ServicesEffect>()
    val effect = _effect.asSharedFlow()

    fun reduce(event: ServicesEvent) {
        when (event) {
            is ServicesEvent.GetAllServices -> loadAllServices()
            is ServicesEvent.OnBackClicked -> navigateBack()
        }
    }

    private fun loadAllServices() {
        _servicesState.value = ServicesState.Loading

        viewModelScope.launch {
            runCatching {
                delay(1000)
                getAllServicesDataUseCase()
            }.fold(
                onSuccess = {
                        services ->
                    _servicesState.value = ServicesState.Success(services)
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
                _servicesState.value = ServicesState.Error.FieldError(
                    resourceProvider.getString(R.string.no_services)
                )
                null
            }
            is NetworkException -> {
                _servicesState.value = ServicesState.Error.GlobalError
                resourceProvider.getString(R.string.error_network)
            }
            is ServerException -> {
                _servicesState.value = ServicesState.Error.GlobalError
                throwable.message ?: resourceProvider
                    .getString(R.string.error_server)
            }
            else -> {
                _servicesState.value = ServicesState.Error.GlobalError
                resourceProvider
                    .getString(R.string.error_unknown)
            }
        }
        if (message != null) {
            _effect.emit(ServicesEffect.ShowErrorDialog(message))
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

    private fun navigateBack() {
        navMain.goBack()
    }

    override fun onCleared() {
        super.onCleared()
    }
}