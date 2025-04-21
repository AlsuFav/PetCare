package ru.fav.petcare.presentation.screens.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.fav.petcare.domain.exceptions.AuthException
import ru.fav.petcare.domain.exceptions.NetworkException
import ru.fav.petcare.domain.exceptions.ServerException
import ru.fav.petcare.domain.models.JwtModel
import ru.fav.petcare.domain.usecases.LoginClientUseCase
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val loginClientUseCase: LoginClientUseCase
): ViewModel() {
    private val _jwtFlow = MutableStateFlow<JwtModel?>(null)
    val currentWeatherFlow = _jwtFlow.asStateFlow()

    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow = _loadingFlow.asStateFlow()

    private val _errorFlow = MutableSharedFlow<String>(replay = 0)
    val errorFlow = _errorFlow.asSharedFlow()

    fun getJwt(phone: String, password: String) {
        _loadingFlow.value = true
        viewModelScope.launch {
            runCatching {
                loginClientUseCase(phone, password)
            }.onSuccess { jwtModel ->
                _jwtFlow.value = jwtModel
                _loadingFlow.value = false
            }.onFailure { throwable ->
                val errorMessage = when (throwable) {
                    is AuthException -> throwable.message ?: "Неверные данные"
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