package ru.fav.petcare.presentation.screens.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.fav.petcare.domain.exceptions.InvalidCredentialsException
import ru.fav.petcare.domain.exceptions.NetworkException
import ru.fav.petcare.domain.exceptions.ServerException
import ru.fav.petcare.domain.usecases.LoginClientUseCase
import ru.fav.petcare.presentation.utils.validators.PhoneValidator
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val loginClientUseCase: LoginClientUseCase
) : ViewModel() {

    private val _authorizationState = MutableStateFlow<AuthorizationState>(AuthorizationState.Initial)
    val authorizationState = _authorizationState.asStateFlow()

    fun authorize(phone: String, password: String) {
        validateInputs(phone, password)?.let { errorMessage ->
            _authorizationState.value = AuthorizationState.Error.FieldError(errorMessage)
            return
        }

        _authorizationState.value = AuthorizationState.Loading

        viewModelScope.launch {
            runCatching {
                loginClientUseCase(phone, password)
            }.fold(
                onSuccess = { jwt -> _authorizationState.value = AuthorizationState.Success },
                onFailure = { e -> _authorizationState.value = handleError(e) }
            )
        }
    }

    private fun validateInputs(phone: String, password: String): String? {
        return when {
            phone.isEmpty() || password.isEmpty() -> "Заполните все поля"
            !PhoneValidator.isValid(phone) -> "Неверный формат номера телефона"
            else -> null
        }
    }

    private fun handleError(throwable: Throwable): AuthorizationState.Error {
        return when (throwable) {
            is InvalidCredentialsException ->
                AuthorizationState.Error.FieldError(
                    throwable.message ?: "Неверный логин или пароль"
                )

            is NetworkException ->
                AuthorizationState.Error.GlobalError("Нет подключения к интернету")

            is ServerException ->
                AuthorizationState.Error.GlobalError(
                    throwable.message ?: "Ошибка сервера"
                )

            else ->
                AuthorizationState.Error.GlobalError("Неизвестная ошибка")
        }
    }
}