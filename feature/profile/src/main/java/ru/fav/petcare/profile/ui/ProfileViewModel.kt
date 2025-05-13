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
import ru.fav.petcare.domain.exception.ClientAlreadyExistsException
import ru.fav.petcare.domain.exception.NetworkException
import ru.fav.petcare.domain.exception.NotFoundException
import ru.fav.petcare.domain.exception.ServerException
import ru.fav.petcare.domain.exception.UnauthorizedException
import ru.fav.petcare.domain.provider.ResourceProvider
import ru.fav.petcare.domain.usecase.ClearJwtUseCase
import ru.fav.petcare.domain.usecase.GetClientDataUseCase
import ru.fav.petcare.domain.usecase.UpdateClientDataUseCase
import ru.fav.petcare.navigation.NavMain
import ru.fav.petcare.profile.ui.state.ClearJwtState
import ru.fav.petcare.profile.ui.state.ProfileEffect
import ru.fav.petcare.profile.ui.state.ProfileEvent
import ru.fav.petcare.profile.ui.state.UpdateProfileState
import ru.fav.petcare.presentation.R
import ru.fav.petcare.presentation.util.validators.PhoneValidator
import ru.fav.petcare.profile.ui.state.ProfileState
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getClientDataUseCase: GetClientDataUseCase,
    private val updateClientDataUseCase: UpdateClientDataUseCase,
    private val clearJwtUseCase: ClearJwtUseCase,
    private val resourceProvider: ResourceProvider,
    private val navMain: NavMain,
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState = _profileState.asStateFlow()

    private val _updateProfileState = MutableStateFlow<UpdateProfileState>(UpdateProfileState.Initial)
    val updateProfileState = _updateProfileState.asStateFlow()

    private val _clearJwtState = MutableStateFlow<ClearJwtState>(ClearJwtState.Initial)
    val clearJwtState = _clearJwtState.asStateFlow()

    private val _effect = MutableSharedFlow<ProfileEffect>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect = _effect.asSharedFlow()

    fun reduce(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.GetClientData -> loadClientData()
            is ProfileEvent.OnSaveClicked -> updateClientData(
                firstName = event.firstName,
                lastName = event.lastName,
                phone = event.phone
            )
            is ProfileEvent.OnSafetyClicked -> navigateToSafety()
            is ProfileEvent.OnLogOutClicked -> deleteJwt()
        }
    }

    private fun loadClientData() {
        _profileState.value = ProfileState.Loading
        viewModelScope.launch {
            runCatching {
                getClientDataUseCase()
            }.fold(
                onSuccess = {
                    client ->
                        _profileState.value = ProfileState.Success(client)
                },
                onFailure = { e ->
                        if (e is UnauthorizedException || e is NotFoundException) {
                            deleteJwt()
                            navigateToAuthorization()
                        }
                        else _profileState.value = handleProfileStateError(e)
                }
            )
        }
    }

    private fun updateClientData(
        firstName: String,
        lastName: String,
        phone: String
    ) {
        validateInputs(
            firstName = firstName,
            lastName = lastName,
            phone = phone
        )?.let { errorMessage ->
            _updateProfileState.value = UpdateProfileState.Error.FieldError(errorMessage)
            return
        }

        _updateProfileState.value = UpdateProfileState.Loading

        viewModelScope.launch {
            runCatching {
                updateClientDataUseCase(
                    firstName = firstName,
                    lastName = lastName,
                    phone = phone
                )
            }.fold(
                onSuccess = {
                    _updateProfileState.value = UpdateProfileState.Success
                    _effect.emit(ProfileEffect.ShowToast(
                        resourceProvider.getString(ru.fav.petcare.profile.R.string.data_updated_successfully))
                    )
                },
                onFailure = { e ->
                    if (e is UnauthorizedException || e is NotFoundException) {
                        deleteJwt()
                        navigateToAuthorization()
                    }
                    else _updateProfileState.value = handleUpdateProfileStateError(e)
                }
            )
        }
    }

    private fun deleteJwt() {
        _clearJwtState.value = ClearJwtState.Loading

        viewModelScope.launch {
            runCatching {
                clearJwtUseCase()
            }.fold(
                onSuccess = {
                    _clearJwtState.value = ClearJwtState.Success
                    navigateToAuthorization()
                },
                onFailure = { _clearJwtState.value =
                    ClearJwtState.Error(resourceProvider.getString(
                        R.string.error_unknown)) }
            )
        }
    }

    private fun validateInputs(
        firstName: String,
        lastName: String,
        phone: String
    ): String? {
        return when {
            lastName.isEmpty() || firstName.isEmpty() || phone.isEmpty() -> resourceProvider.getString(R.string.error_fill_all_fields)
            !PhoneValidator.isValid(phone) -> resourceProvider.getString(R.string.error_invalid_phone_format)
            else -> null
        }
    }

    private fun handleProfileStateError(throwable: Throwable): ProfileState.Error {
        return when (throwable) {

            is NetworkException ->
                ProfileState.Error(resourceProvider.getString(R.string.error_network))

            is ServerException ->
                ProfileState.Error(
                    throwable.message ?: resourceProvider.getString(R.string.error_server)
                )

            else ->
                ProfileState.Error(resourceProvider.getString(R.string.error_unknown))
        }
    }

    private fun handleUpdateProfileStateError(throwable: Throwable): UpdateProfileState.Error {
        return when (throwable) {
            is ClientAlreadyExistsException ->
                UpdateProfileState.Error.FieldError(resourceProvider.getString(R.string.error_client_already_exists))

            is NetworkException ->
                UpdateProfileState.Error.GlobalError(resourceProvider.getString(R.string.error_network))

            is ServerException ->
                UpdateProfileState.Error.GlobalError(
                    throwable.message ?: resourceProvider.getString(R.string.error_server)
                )

            else ->
                UpdateProfileState.Error.GlobalError(resourceProvider.getString(R.string.error_unknown))
        }
    }

    private fun navigateToAuthorization() {
        navMain.goToAuthorizationPage()
    }

    private fun navigateToSafety() {
        navMain.goToSafetyPage()
    }

    override fun onCleared() {
        super.onCleared()
    }
}
