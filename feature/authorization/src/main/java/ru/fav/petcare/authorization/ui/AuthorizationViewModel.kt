package ru.fav.petcare.authorization.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.fav.petcare.authorization.ui.state.AuthorizationEvent
import ru.fav.petcare.authorization.ui.state.AuthorizationState
import ru.fav.petcare.domain.exception.InvalidCredentialsException
import ru.fav.petcare.domain.exception.NetworkException
import ru.fav.petcare.domain.exception.ServerException
import ru.fav.petcare.domain.provider.ResourceProvider
import ru.fav.petcare.domain.usecase.client.LoginClientUseCase
import ru.fav.petcare.domain.usecase.jwt.SaveJwtUseCase
import ru.fav.petcare.navigation.NavMain
import ru.fav.petcare.presentation.R
import ru.fav.petcare.presentation.util.validators.PhoneValidator
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val loginClientUseCase: LoginClientUseCase,
    private val saveJwtUseCase: SaveJwtUseCase,
    private val resourceProvider: ResourceProvider,
    private val navMain: NavMain,
) : ViewModel() {

    private val _authorizationState = MutableStateFlow<AuthorizationState>(AuthorizationState.Initial)
    val authorizationState = _authorizationState.asStateFlow()

    fun reduce(event: AuthorizationEvent) {
        when (event) {
            is AuthorizationEvent.OnSignInClicked -> authorize(event.phone, event.password)
            is AuthorizationEvent.OnSignUpClicked -> navigateToRegistration()
        }
    }

    private fun authorize(phone: String, password: String) {
        validateInputs(phone, password)?.let { errorMessage ->
            _authorizationState.value = AuthorizationState.Error.FieldError(errorMessage)
            return
        }

        _authorizationState.value = AuthorizationState.Loading

        viewModelScope.launch {
            runCatching {
                val jwt = loginClientUseCase(phone, password)
                saveJwtUseCase(jwt)
            }.fold(
                onSuccess = {
                    _authorizationState.value = AuthorizationState.Success
                    navigateToHome()
                },
                onFailure = { e -> _authorizationState.value = handleError(e) }
            )
        }
    }

    private fun validateInputs(phone: String, password: String): String? {
        return when {
            phone.isEmpty() || password.isEmpty() -> resourceProvider.getString(R.string.error_fill_all_fields)
            !PhoneValidator.isValid(phone) -> resourceProvider.getString(R.string.error_invalid_phone_format)
            else -> null
        }
    }

    private fun handleError(throwable: Throwable): AuthorizationState.Error {
        return when (throwable) {
            is InvalidCredentialsException ->
                AuthorizationState.Error.FieldError(
                    throwable.message ?: resourceProvider.getString(R.string.error_invalid_credentials)
                )

            is NetworkException ->
                AuthorizationState.Error.GlobalError(resourceProvider.getString(R.string.error_network))

            is ServerException ->
                AuthorizationState.Error.GlobalError(
                    throwable.message ?: resourceProvider.getString(R.string.error_server)
                )

            else ->
                AuthorizationState.Error.GlobalError(resourceProvider.getString(R.string.error_unknown))
        }
    }

    private fun navigateToRegistration() {
        navMain.goToRegistrationPage()
    }

    private fun navigateToHome() {
        navMain.goToHomePage()
    }
}