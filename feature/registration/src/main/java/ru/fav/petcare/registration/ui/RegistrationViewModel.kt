package ru.fav.petcare.registration.ui

import android.provider.Settings.Global.getString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.fav.petcare.domain.exception.ClientAlreadyExistsException
import ru.fav.petcare.domain.exception.NetworkException
import ru.fav.petcare.domain.exception.ServerException
import ru.fav.petcare.domain.provider.ResourceProvider
import ru.fav.petcare.domain.usecase.RegisterClientUseCase
import ru.fav.petcare.domain.usecase.SaveJwtUseCase
import ru.fav.petcare.navigation.NavMain
import ru.fav.petcare.presentation.R
import ru.fav.petcare.presentation.util.validators.PhoneValidator
import ru.fav.petcare.registration.ui.state.RegistrationEvent
import ru.fav.petcare.registration.ui.state.RegistrationState
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registerClientUseCase: RegisterClientUseCase,
    private val saveJwtUseCase: SaveJwtUseCase,
    private val resourceProvider: ResourceProvider,
    private val navMain: NavMain,
): ViewModel() {

    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Initial)
    val registrationState = _registrationState.asStateFlow()

    fun reduce(event: RegistrationEvent) {
        when (event) {
            is RegistrationEvent.OnSignUpClicked -> register(
                firstName = event.firstName,
                lastName = event.lastName,
                phone = event.phone,
                password = event.password,
                confirmPassword = event.confirmPassword)
            is RegistrationEvent.OnSignInClicked -> navigateToAuthorization()
        }
    }

    private fun register(
        firstName: String,
        lastName: String,
        phone: String,
        password: String,
        confirmPassword: String
    ){
        validateInputs(
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            password = password,
            confirmPassword = confirmPassword
        )?.let { errorMessage ->
            _registrationState.value = RegistrationState.Error.FieldError(errorMessage)
            return
        }

        _registrationState.value = RegistrationState.Loading

        viewModelScope.launch {
            runCatching {
                val jwt = registerClientUseCase(
                    firstName = firstName,
                    lastName = lastName,
                    phone = phone,
                    password = password,
                    confirmPassword = confirmPassword
                )
                saveJwtUseCase(jwt)
            }.fold(
                onSuccess = {
                    _registrationState.value = RegistrationState.Success
                    navigateToHome()
                },
                onFailure = { e -> _registrationState.value = handleError(e) }
            )
        }
    }

    private fun validateInputs(
        firstName: String,
        lastName: String,
        phone: String,
        password: String,
        confirmPassword: String
    ): String? {
        return when {
            lastName.isEmpty() || firstName.isEmpty() || phone.isEmpty()
                    || password.isEmpty() || confirmPassword.isEmpty() -> resourceProvider.getString(R.string.error_fill_all_fields)
            !PhoneValidator.isValid(phone) -> resourceProvider.getString(R.string.error_invalid_phone_format)
            password != confirmPassword -> resourceProvider.getString(R.string.error_passwords_are_not_the_same)
            else -> null
        }
    }

    private fun handleError(throwable: Throwable): RegistrationState.Error {
        return when (throwable) {
            is ClientAlreadyExistsException ->
                RegistrationState.Error.FieldError(
                    throwable.message ?: resourceProvider.getString(R.string.error_client_already_exists)
                )

            is NetworkException ->
                RegistrationState.Error.GlobalError(resourceProvider.getString(R.string.error_network))

            is ServerException ->
                RegistrationState.Error.GlobalError(
                    throwable.message ?: resourceProvider.getString(R.string.error_server)
                )

            else ->
                RegistrationState.Error.GlobalError(resourceProvider.getString(R.string.error_unknown))
        }
    }

    private fun navigateToAuthorization() {
        navMain.goToAuthorizationPage()
    }

    private fun navigateToHome() {
        navMain.goToHomePage()
    }

    override fun onCleared() {
        super.onCleared()
    }
}