package ru.fav.petcare.presentation.screens.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.fav.petcare.domain.exceptions.ClientAlreadyExistsException
import ru.fav.petcare.domain.exceptions.NetworkException
import ru.fav.petcare.domain.exceptions.ServerException
import ru.fav.petcare.domain.usecases.RegisterClientUseCase
import ru.fav.petcare.presentation.utils.validators.PhoneValidator
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registerClientUseCase: RegisterClientUseCase
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
            lastName.isEmpty() || firstName.isEmpty() || phone.isEmpty() || password.isEmpty() -> "Заполните все поля"
            !PhoneValidator.isValid(phone) -> "Неверный формат номера телефона"
            password != confirmPassword -> "Пароли не совпадают"
            else -> null
        }
    }

    private fun handleError(throwable: Throwable): RegistrationState.Error {
        return when (throwable) {
            is ClientAlreadyExistsException ->
                RegistrationState.Error.FieldError(
                    throwable.message ?: "Пользователь с таким номером уже существует"
                )

            is NetworkException ->
                RegistrationState.Error.GlobalError("Нет подключения к интернету")

            is ServerException ->
                RegistrationState.Error.GlobalError(
                    throwable.message ?: "Ошибка сервера"
                )

            else ->
                RegistrationState.Error.GlobalError("Неизвестная ошибка")
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}