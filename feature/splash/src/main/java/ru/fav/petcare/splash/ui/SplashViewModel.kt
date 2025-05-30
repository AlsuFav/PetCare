package ru.fav.petcare.splash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.fav.petcare.domain.exception.NoJwtException
import ru.fav.petcare.domain.provider.ResourceProvider
import ru.fav.petcare.domain.usecase.jwt.GetJwtUseCase
import ru.fav.petcare.navigation.NavMain
import ru.fav.petcare.presentation.R
import ru.fav.petcare.splash.ui.state.SplashEvent
import ru.fav.petcare.splash.ui.state.SplashState
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getJwtUseCase: GetJwtUseCase,
    private val resourceProvider: ResourceProvider,
    private val navMain: NavMain,
) : ViewModel() {

    private val _splashState = MutableStateFlow<SplashState>(SplashState.Loading)
    val splashState = _splashState.asStateFlow()

    fun reduce(event: SplashEvent) {
        when (event) {
            is SplashEvent.CheckApiKey -> getApiKey()
        }
    }

    private fun getApiKey() {
        viewModelScope.launch {
            runCatching {
                getJwtUseCase()
            }.fold(
                onSuccess = {
                    _splashState.value = SplashState.Success
                    navigateToHome()
                },
                onFailure = { e -> _splashState.value = handleError(e) }
            )
        }
    }


    private fun handleError(throwable: Throwable): SplashState.Error {
        return when (throwable) {
            is NoJwtException -> {
                navigateToAuthorization()
                SplashState.Error.NoApiKey
            }

            else ->
                SplashState.Error.GlobalError(resourceProvider.getString(R.string.error_unknown))
        }
    }

    private fun navigateToHome() {
        navMain.goToHomePage()
    }

    private fun navigateToAuthorization() {
        navMain.goToAuthorizationPage()
    }

    override fun onCleared() {
        super.onCleared()
    }
}
