package ru.fav.petcare.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.fav.petcare.domain.provider.ResourceProvider
import ru.fav.petcare.domain.usecase.ClearJwtUseCase
import ru.fav.petcare.domain.usecase.SaveJwtUseCase
import ru.fav.petcare.navigation.NavMain
import ru.fav.petcare.profile.ui.state.ClearJwtState
import ru.fav.petcare.profile.ui.state.ProfileEffect
import ru.fav.petcare.profile.ui.state.ProfileEvent
import ru.fav.petcare.profile.ui.state.UpdateProfileState
import ru.fav.petcare.presentation.R
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val clearJwtUseCase: ClearJwtUseCase,
    private val resourceProvider: ResourceProvider,
    private val navMain: NavMain,
) : ViewModel() {

    private val _updateProfileState = MutableStateFlow<UpdateProfileState>(UpdateProfileState.Initial)
    val updateProfileState = _updateProfileState.asStateFlow()

    private val _clearApiKeyState = MutableStateFlow<ClearJwtState>(ClearJwtState.Initial)
    val clearApiKeyState = _clearApiKeyState.asStateFlow()

    private val _effect = MutableSharedFlow<ProfileEffect>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect = _effect.asSharedFlow()

    fun reduce(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.OnLogOutClicked -> deleteApiKey()
        }
    }

    private fun deleteApiKey() {
        _clearApiKeyState.value = ClearJwtState.Loading

        viewModelScope.launch {
            runCatching {
                clearJwtUseCase()
            }.fold(
                onSuccess = {
                    _clearApiKeyState.value = ClearJwtState.Success
                    navigateToAuthorization()
                },
                onFailure = { _clearApiKeyState.value =
                    ClearJwtState.Error(resourceProvider.getString(
                        R.string.error_unknown)) }
            )
        }
    }

    private fun navigateToAuthorization() {
        navMain.goToAuthorizationPage()
    }

    override fun onCleared() {
        super.onCleared()
    }
}
