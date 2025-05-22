package ru.fav.petcare.safety.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.fav.petcare.domain.exception.InvalidPasswordException
import ru.fav.petcare.domain.exception.NetworkException
import ru.fav.petcare.domain.exception.NotFoundException
import ru.fav.petcare.domain.exception.ServerException
import ru.fav.petcare.domain.exception.UnauthorizedException
import ru.fav.petcare.domain.provider.ResourceProvider
import ru.fav.petcare.domain.usecase.ChangePasswordUseCase
import ru.fav.petcare.domain.usecase.ClearJwtUseCase
import ru.fav.petcare.domain.usecase.DeleteClientUseCase
import ru.fav.petcare.navigation.NavMain
import ru.fav.petcare.presentation.R
import ru.fav.petcare.safety.ui.state.DeleteClientState
import ru.fav.petcare.safety.ui.state.SafetyEffect
import ru.fav.petcare.safety.ui.state.SafetyEffect.*
import ru.fav.petcare.safety.ui.state.SafetyEvent
import ru.fav.petcare.safety.ui.state.UpdatePasswordState
import javax.inject.Inject

@HiltViewModel
class SafetyViewModel @Inject constructor(
    private val updatePasswordUseCase: ChangePasswordUseCase,
    private val deleteClientUseCase: DeleteClientUseCase,
    private val clearJwtUseCase: ClearJwtUseCase,
    private val resourceProvider: ResourceProvider,
    private val navMain: NavMain,
) : ViewModel() {

    private val _updatePasswordState = MutableStateFlow<UpdatePasswordState>(UpdatePasswordState.Initial)
    val updatePasswordState = _updatePasswordState.asStateFlow()

    private val _deleteClientState = MutableStateFlow<DeleteClientState>(DeleteClientState.Initial)
    val deleteClientState = _deleteClientState.asStateFlow()

    private val _effect = MutableSharedFlow<SafetyEffect>()
    val effect = _effect.asSharedFlow()

    fun reduce(event: SafetyEvent) {
        viewModelScope.launch {
            when (event) {
                is SafetyEvent.OnBackClicked -> navigateBack()
                is SafetyEvent.OnChangePasswordClicked ->
                    updatePassword(
                        currentPassword = event.currentPassword,
                        newPassword = event.newPassword,
                        confirmNewPassword = event.confirmNewPassword
                    )
                is SafetyEvent.OnDeleteAccountClicked -> {
                    _effect.emit(
                        ShowDeleteAccountConfirmation(
                            resourceProvider.getString(ru.fav.petcare.safety.R.string.confirm_delete_account_message)
                        )
                    )
                }
                SafetyEvent.OnConfirmDeleteAccountClicked -> deleteClient()
            }
        }
    }

    private fun updatePassword(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ) {
        _updatePasswordState.value = UpdatePasswordState.Loading

        validateInputs(
            currentPassword = currentPassword,
            newPassword = newPassword,
            confirmNewPassword = confirmNewPassword
        )?.let { errorMessage ->
            _updatePasswordState.value = UpdatePasswordState.Error.FieldError(errorMessage)
            return
        }

        viewModelScope.launch {
            runCatching {
                updatePasswordUseCase(
                    currentPassword = currentPassword,
                    newPassword = newPassword,
                    confirmNewPassword = confirmNewPassword
                )
            }.fold(
                onSuccess = {
                    _updatePasswordState.value = UpdatePasswordState.Success
                    _effect.emit(
                        ShowToast(
                            resourceProvider.getString(ru.fav.petcare.safety.R.string.password_updated_successfully))
                    )
                },
                onFailure = { e ->
                    if (e is UnauthorizedException || e is NotFoundException) {
                        deleteJwt()
                        navigateToAuthorization()
                    } else _updatePasswordState.value = handleUpdatePasswordStateError(e)
                }
            )
        }
    }

    private fun deleteClient() {
        _deleteClientState.value = DeleteClientState.Loading

        viewModelScope.launch {
            runCatching {
                deleteClientUseCase()
                clearJwtUseCase()
            }.fold(
                onSuccess = {
                    _effect.emit(
                        ShowToast(
                            resourceProvider.getString(ru.fav.petcare.safety.R.string.account_deleted_successfully))
                    )
                    _deleteClientState.value = DeleteClientState.Success
                    navigateToAuthorization()
                },
                onFailure = { e ->
                    if (e is UnauthorizedException || e is NotFoundException) {
                        deleteJwt()
                        navigateToAuthorization()
                    } else _deleteClientState.value = handleDeleteClientStateError(e)
                }
            )
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

    private fun validateInputs(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): String? {
        return when {
            currentPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty() -> resourceProvider.getString(R.string.error_fill_all_fields)
            newPassword != confirmNewPassword -> resourceProvider.getString(R.string.error_passwords_are_not_the_same)
            else -> null
        }
    }

    private fun handleDeleteClientStateError(throwable: Throwable): DeleteClientState.Error {
        return when (throwable) {

            is NetworkException ->
                DeleteClientState.Error(resourceProvider.getString(R.string.error_network))

            is ServerException ->
                DeleteClientState.Error(
                    throwable.message ?: resourceProvider.getString(R.string.error_server)
                )

            else ->
                DeleteClientState.Error(resourceProvider.getString(R.string.error_unknown))
        }
    }

    private fun handleUpdatePasswordStateError(throwable: Throwable): UpdatePasswordState.Error {
        return when (throwable) {
            is InvalidPasswordException ->
                UpdatePasswordState.Error.FieldError(
                    throwable.message ?: resourceProvider.getString(
                        ru.fav.petcare.safety.R.string.error_invalid_current_password)
                )

            is NetworkException ->
                UpdatePasswordState.Error.GlobalError(resourceProvider.getString(R.string.error_network))

            is ServerException ->
                UpdatePasswordState.Error.GlobalError(
                    throwable.message ?: resourceProvider.getString(R.string.error_server)
                )

            else ->
                UpdatePasswordState.Error.GlobalError(resourceProvider.getString(R.string.error_unknown))
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
