package ru.fav.petcare.pet.details.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.fav.petcare.domain.exception.ForbiddenAccessException
import ru.fav.petcare.domain.exception.NetworkException
import ru.fav.petcare.domain.exception.NotFoundException
import ru.fav.petcare.domain.exception.ServerException
import ru.fav.petcare.domain.exception.UnauthorizedException
import ru.fav.petcare.domain.provider.DateProvider
import ru.fav.petcare.domain.provider.ResourceProvider
import ru.fav.petcare.domain.usecase.jwt.ClearJwtUseCase
import ru.fav.petcare.domain.usecase.pet.DeletePetUseCase
import ru.fav.petcare.domain.usecase.pet.GetPetDataUseCase
import ru.fav.petcare.domain.usecase.pet.UpdatePetUseCase
import ru.fav.petcare.navigation.NavMain
import ru.fav.petcare.pet.details.ui.state.DateState
import ru.fav.petcare.pet.details.ui.state.DeletePetState
import ru.fav.petcare.pet.details.ui.state.PetDetailsEffect
import ru.fav.petcare.pet.details.ui.state.PetDetailsEffect.ShowDeletePetConfirmation
import ru.fav.petcare.pet.details.ui.state.PetDetailsEffect.ShowToast
import ru.fav.petcare.pet.details.ui.state.PetDetailsEvent
import ru.fav.petcare.pet.details.ui.state.PetDetailsState
import ru.fav.petcare.pet.details.ui.state.UpdatePetState
import ru.fav.petcare.presentation.R
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class PetDetailsViewModel @Inject constructor(
    private val getPetDataUseCase: GetPetDataUseCase,
    private val updatePetUseCase: UpdatePetUseCase,
    private val deletePetUseCase: DeletePetUseCase,
    private val clearJwtUseCase: ClearJwtUseCase,
    private val dateProvider: DateProvider,
    private val resourceProvider: ResourceProvider,
    private val navMain: NavMain,
) : ViewModel() {

    private val _petDetailsState = MutableStateFlow<PetDetailsState>(PetDetailsState.Loading)
    val petDetailsState = _petDetailsState.asStateFlow()

    private val _updatePetState = MutableStateFlow<UpdatePetState>(UpdatePetState.Initial)
    val updatePetState = _updatePetState.asStateFlow()

    private val _deletePetState = MutableStateFlow<DeletePetState>(DeletePetState.Initial)
    val deletePetState = _deletePetState.asStateFlow()

    private val _dateState = MutableStateFlow(DateState())
    val dateState: StateFlow<DateState> = _dateState.asStateFlow()

    private val _effect = MutableSharedFlow<PetDetailsEffect>()
    val effect = _effect.asSharedFlow()

    fun reduce(event: PetDetailsEvent) {
        viewModelScope.launch {
            when (event) {
                is PetDetailsEvent.GetPetData -> loadPetData(event.id)
                is PetDetailsEvent.OnSaveClicked -> updatePetData(
                    id = event.id,
                    name = event.name,
                    birthDate = event.birthDate
                )

                is PetDetailsEvent.OnBackClicked -> navigateBack()
                is PetDetailsEvent.OnDateClicked -> showDatePicker()
                is PetDetailsEvent.OnDateSelected -> onDateSelected(event.calendar)
                is PetDetailsEvent.OnDeletePetClicked -> {
                    _effect.emit(
                        ShowDeletePetConfirmation(
                            resourceProvider.getString(ru.fav.petcare.pet.details.R.string.confirm_delete_pet_message)
                        )
                    )
                }

                is PetDetailsEvent.OnConfirmDeletePetClicked -> deletePet(event.id)
            }
        }
    }

    private fun loadPetData(id: Long) {
        _petDetailsState.value = PetDetailsState.Loading
        viewModelScope.launch {
            runCatching {
                getPetDataUseCase(id)
            }.fold(
                onSuccess = {
                        pet ->
                    _petDetailsState.value = PetDetailsState.Success(pet)
                },
                onFailure = { e ->
                    if (e is UnauthorizedException) {
                        deleteJwt()
                        navigateToAuthorization()
                    } else if (e is NotFoundException || e is ForbiddenAccessException) {
                        navigateToAllPets()
                    }
                    else {
                        handleError(e)
                        _petDetailsState.value = PetDetailsState.Error
                    }
                }
            )
        }
    }

    private fun updatePetData(
        id: Long,
        name: String,
        birthDate: String
    ) {
        _updatePetState.value = UpdatePetState.Loading

        validateInputs(
            name = name,
            birthDate = birthDate,
        )?.let { errorMessage ->
            _updatePetState.value = UpdatePetState.Error.FieldError(errorMessage)
            return
        }

        viewModelScope.launch {
            runCatching {
                updatePetUseCase(
                    id = id,
                    name = name,
                    birthDate = birthDate,
                )
            }.fold(
                onSuccess = {
                    _updatePetState.value = UpdatePetState.Success
                    _effect.emit(
                        ShowToast(
                            resourceProvider.getString(ru.fav.petcare.pet.details.R.string.pet_data_updated_successfully))
                    )
                },
                onFailure = { e ->
                    if (e is UnauthorizedException) {
                        deleteJwt()
                        navigateToAuthorization()
                    } else if (e is NotFoundException || e is ForbiddenAccessException) {
                        navigateToAllPets()
                    }
                    else {
                        handleError(e)
                        _updatePetState.value = UpdatePetState.Error.GlobalError
                    }
                }
            )
        }
    }

    private fun deletePet(id: Long) {
        _deletePetState.value = DeletePetState.Loading

        viewModelScope.launch {
            runCatching {
                deletePetUseCase(id)
            }.fold(
                onSuccess = {
                    _effect.emit(
                        ShowToast(
                            resourceProvider.getString(ru.fav.petcare.pet.details.R.string.pet_deleted_successfully))
                    )
                    _deletePetState.value = DeletePetState.Success
                    navigateToAllPets()
                },
                onFailure = { e ->
                    if (e is UnauthorizedException) {
                        deleteJwt()
                        navigateToAuthorization()
                    } else if (e is NotFoundException || e is ForbiddenAccessException) {
                        navigateToAllPets()
                    }
                    else {
                        handleError(e)
                        _deletePetState.value = DeletePetState.Error
                    }
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
        name: String,
        birthDate: String
    ): String? {
        return when {
            name.isEmpty() || birthDate.isEmpty() -> resourceProvider.getString(R.string.error_fill_all_fields)
            else -> null
        }
    }

    private suspend fun handleError(throwable: Throwable) {
        val message =  when (throwable) {
            is NetworkException ->
                resourceProvider.getString(R.string.error_network)
            is ServerException ->
               throwable.message ?: resourceProvider.getString(R.string.error_server)
            else ->
                resourceProvider.getString(R.string.error_unknown)
        }

        _effect.emit(PetDetailsEffect.ShowErrorDialog(message))
    }

    private fun onDateSelected(calendar: Calendar) {
        val date = dateProvider.formatDate(calendar)
        _dateState.update {
            it.copy(
                date = date,
            )
        }
    }

    private fun showDatePicker() {
        viewModelScope.launch {
            val initialCalendar = if (_dateState.value.date.isEmpty()) {
                dateProvider.getCurrentDate()
            } else {
                dateProvider.parseDate(_dateState.value.date)
            }

            val minDateCalendar = Calendar.getInstance().apply {
                set(2000, Calendar.JANUARY, 1)
            }

            _effect.emit(PetDetailsEffect.ShowDatePicker(
                maxDateMillis = dateProvider.getCurrentDate().timeInMillis,
                minDateMillis = minDateCalendar.timeInMillis,
                initialDate = initialCalendar
            ))
        }
    }

    private fun navigateToAuthorization() {
        navMain.goToAuthorizationPage()
    }

    private fun navigateToAllPets() {
        navMain.goToAllPetsPage()
    }

    private fun navigateBack() {
        navMain.goBack()
    }

    override fun onCleared() {
        super.onCleared()
    }
}
