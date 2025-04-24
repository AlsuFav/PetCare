package ru.fav.petcare.presentation.screens.registration

import android.provider.Settings.Global.getString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.fav.petcare.R
import ru.fav.petcare.domain.exceptions.ClientAlreadyExistsException
import ru.fav.petcare.domain.exceptions.NetworkException
import ru.fav.petcare.domain.exceptions.ServerException
import ru.fav.petcare.domain.providers.ResourceProvider
import ru.fav.petcare.domain.usecases.RegisterClientUseCase
import ru.fav.petcare.presentation.utils.validators.PhoneValidator
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registerClientUseCase: RegisterClientUseCase,
    private val resourceProvider: ResourceProvider
): ViewModel() {

    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Initial)
    val registrationState = _registrationState.asStateFlow()

    fun register(
        firstName: String,
        lastName: String,
        phone: String,
        password: String,
        confirmPassword: String
    ){
        validateInputs(firstName, lastName, phone, password, confirmPassword)?.let { errorMessage ->
            _registrationState.value = RegistrationState.Error.FieldError(errorMessage)
            return
        }

        _registrationState.value = RegistrationState.Loading

        viewModelScope.launch {
            runCatching {
                registerClientUseCase(firstName, lastName, phone, password, confirmPassword)
            }.fold(
                onSuccess = { jwt -> _registrationState.value = RegistrationState.Success },
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
            lastName.isEmpty() || firstName.isEmpty() || phone.isEmpty() || password.isEmpty() -> resourceProvider.getString(R.string.error_fill_all_fields)
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

    override fun onCleared() {
        super.onCleared()
    }
}