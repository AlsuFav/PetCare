package ru.fav.petcare.pet.add

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
import ru.fav.petcare.domain.exception.NetworkException
import ru.fav.petcare.domain.exception.ServerException
import ru.fav.petcare.domain.exception.UnauthorizedException
import ru.fav.petcare.domain.provider.DateProvider
import ru.fav.petcare.domain.provider.ResourceProvider
import ru.fav.petcare.domain.usecase.jwt.ClearJwtUseCase
import ru.fav.petcare.domain.usecase.pet.CreatePetUseCase
import ru.fav.petcare.domain.usecase.pet.GetAllBreedsUseCase
import ru.fav.petcare.navigation.NavMain
import ru.fav.petcare.pet.add.state.AddPetEffect
import ru.fav.petcare.pet.add.state.AddPetState
import ru.fav.petcare.pet.add.state.DateState
import ru.fav.petcare.presentation.R
import ru.fav.petcare.pet.add.state.AddPetEvent
import ru.fav.petcare.pet.add.state.BreedsState
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AddPetViewModel @Inject constructor(
    private val getAllBreedsUseCase: GetAllBreedsUseCase,
    private val createPetUseCase: CreatePetUseCase,
    private val clearJwtUseCase: ClearJwtUseCase,
    private val dateProvider: DateProvider,
    private val resourceProvider: ResourceProvider,
    private val navMain: NavMain,
) : ViewModel() {

    private val _dateState = MutableStateFlow(DateState())
    val dateState: StateFlow<DateState> = _dateState.asStateFlow()

    private val _breedsState = MutableStateFlow(BreedsState())
    val breedsState: StateFlow<BreedsState> = _breedsState.asStateFlow()

    private val _addPetState = MutableStateFlow<AddPetState>(AddPetState.Initial)
    val addPetState = _addPetState.asStateFlow()

    private val _effect = MutableSharedFlow<AddPetEffect>()
    val effect = _effect.asSharedFlow()

    fun reduce(event: AddPetEvent) {
        when (event) {
            is AddPetEvent.OnDateClicked -> showDatePicker()
            is AddPetEvent.OnDateSelected -> onDateSelected(event.calendar)
            is AddPetEvent.OnAddPetClicked -> createPet(
                name = event.name,
                species = event.species,
                breed = event.breed,
                birthDate = event.birthDate
            )
            is AddPetEvent.OnCancelClicked -> navigateToAllPets()
            is AddPetEvent.LoadBreeds -> getAllBreeds()
        }
    }

    private fun getAllBreeds() {
        viewModelScope.launch {
            runCatching {
                getAllBreedsUseCase()
            }.fold(
                onSuccess = { breeds ->
                    _breedsState.update {
                        it.copy(
                            breeds = breeds
                        )
                    }
                },
                onFailure = { e -> handleError(e)
                }
            )
        }
    }

    private fun createPet(
        name: String,
        species: String,
        breed: String?,
        birthDate: String
    ){
        _addPetState.value = AddPetState.Loading

        validateInputs(
            name = name,
            species = species,
            breed = breed,
            birthDate = birthDate
        )?.let { errorMessage ->
            _addPetState.value = AddPetState.Error.FieldError(errorMessage)
            return
        }

        viewModelScope.launch {
            runCatching {
                createPetUseCase(
                    name = name,
                    species = species,
                    breed = breed,
                    birthDate = birthDate
                )
            }.fold(
                onSuccess = {
                    _addPetState.value = AddPetState.Success
                    _effect.emit(AddPetEffect.ShowToast(
                        resourceProvider.getString(ru.fav.petcare.pet.R.string.pet_created_successfully)
                    ))
                    navigateToAllPets()
                },
                onFailure = { e ->
                    if (e is UnauthorizedException) {
                        deleteJwt()
                        navigateToAuthorization()
                    } else handleError(e)
                }
            )
        }
    }

    private fun validateInputs(
        name: String,
        species: String,
        breed: String?,
        birthDate: String
    ): String? {
        val dog = resourceProvider.getString(ru.fav.petcare.pet.R.string.dog)

        return when {
            name.isEmpty() || species.isEmpty() || birthDate.isEmpty()
                    || (species == dog && breed.isNullOrEmpty())
            -> resourceProvider.getString(R.string.error_fill_all_fields)
            species == dog && !breedsState.value.breeds.any { it.equals(breed, ignoreCase = true) } ->
                resourceProvider.getString(ru.fav.petcare.pet.R.string.error_invalid_breed)
            else -> null
        }
    }

    private suspend fun handleError(throwable: Throwable) {
        val message = when (throwable) {
            is NetworkException -> {
                resourceProvider.getString(R.string.error_network)
            }
            is ServerException -> {
                throwable.message ?: resourceProvider
                    .getString(R.string.error_server)
            }
            else -> {
                resourceProvider
                    .getString(R.string.error_unknown)
            }
        }

        _addPetState.value = AddPetState.Error.GlobalError
        _effect.emit(AddPetEffect.ShowErrorDialog(message))
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

            _effect.emit(AddPetEffect.ShowDatePicker(
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

    override fun onCleared() {
        super.onCleared()
    }
}
