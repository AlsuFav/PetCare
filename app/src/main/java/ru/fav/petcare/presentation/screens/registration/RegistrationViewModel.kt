package ru.fav.petcare.presentation.screens.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.fav.petcare.domain.exceptions.ClientAlreadyExistsException
import ru.fav.petcare.domain.exceptions.InvalidCredentialsException
import ru.fav.petcare.domain.exceptions.NetworkException
import ru.fav.petcare.domain.exceptions.ServerException
import ru.fav.petcare.domain.models.JwtModel
import ru.fav.petcare.domain.usecases.RegisterClientUseCase
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registerClientUseCase: RegisterClientUseCase
): ViewModel() {
    private val _jwtFlow = MutableStateFlow<JwtModel?>(null)
    val jwtFlow = _jwtFlow.asStateFlow()

    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow = _loadingFlow.asStateFlow()

    private val _errorFlow = MutableSharedFlow<String>(replay = 0)
    val errorFlow = _errorFlow.asSharedFlow()

    fun getJwt(
        firstName: String,
        lastName: String,
        phone: String,
        password: String,
        confirmPassword: String
    ){
        _loadingFlow.value = true
        viewModelScope.launch {
            runCatching {
                registerClientUseCase(firstName, lastName, phone, password, confirmPassword)
            }.onSuccess { jwtModel ->
                _jwtFlow.value = jwtModel
                _loadingFlow.value = false
            }.onFailure { throwable ->
                val errorMessage = when (throwable) {
                    is ClientAlreadyExistsException -> throwable.message ?: "Пользователь с таким номером уже существует"
                    is NetworkException -> "Нет подключения к интернету"
                    is ServerException -> throwable.message ?: "Ошибка сервера"
                    else -> "Неизвестная ошибка"
                }
                _loadingFlow.value = false
                _errorFlow.emit(errorMessage)
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
    }
}