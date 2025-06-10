package ru.fav.petcare.pet.details.ui

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import ru.fav.petcare.pet.details.R
import ru.fav.petcare.pet.details.databinding.FragmentPetDetailsBinding
import kotlin.getValue
import com.bumptech.glide.Glide
import ru.fav.petcare.domain.model.PetModel
import ru.fav.petcare.pet.details.ui.state.DeletePetState
import ru.fav.petcare.pet.details.ui.state.PetDetailsEffect
import ru.fav.petcare.pet.details.ui.state.PetDetailsEvent
import ru.fav.petcare.pet.details.ui.state.PetDetailsState
import ru.fav.petcare.pet.details.ui.state.UpdatePetState
import ru.fav.petcare.util.extension.observe
import ru.fav.petcare.util.extension.observeNotSuspend
import ru.fav.petcare.util.extension.showConfirmationDialog
import ru.fav.petcare.util.extension.showErrorDialog
import java.util.Calendar

@AndroidEntryPoint
class PetDetailsFragment: Fragment(R.layout.fragment_pet_details) {

    private val viewBinding: FragmentPetDetailsBinding by viewBinding(FragmentPetDetailsBinding::bind)

    private val petDetailsViewModel: PetDetailsViewModel by viewModels()

    private val args: PetDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()

        petDetailsViewModel.reduce(event = PetDetailsEvent.GetPetData(args.id))
    }

    private fun initViews() = with(viewBinding) {

        this.editTextBirthDate.setOnClickListener {
            petDetailsViewModel.reduce(PetDetailsEvent.OnDateClicked)
        }

        this.buttonBack.setOnClickListener {
            petDetailsViewModel.reduce(event = PetDetailsEvent.OnBackClicked)
        }

        this.buttonEdit.setOnClickListener {
            showEditMode(true)
        }

        this.buttonDeletePet.setOnClickListener {
            petDetailsViewModel.reduce(event = PetDetailsEvent.OnDeletePetClicked)
        }

        this.buttonSave.setOnClickListener {
            val name = viewBinding.editTextName.text.toString().trim()
            val birthDate = viewBinding.editTextBirthDate.text.toString().trim()

            petDetailsViewModel.reduce(event = PetDetailsEvent.OnSaveClicked(
                id = args.id,
                name = name,
                birthDate = birthDate
            ))
        }

        this.buttonCancel.setOnClickListener {
            hideFieldError()
            showEditMode(false)
            petDetailsViewModel.reduce(event = PetDetailsEvent.GetPetData(args.id))
        }
    }

    private fun observeViewModel() {
        petDetailsViewModel.effect.observeNotSuspend(viewLifecycleOwner) { state ->
            when (state) {
                is PetDetailsEffect.ShowToast -> showToast(state.message)
                is PetDetailsEffect.ShowDatePicker -> showDatePicker(
                    maxDateMillis = state.maxDateMillis,
                    minDateMillis = state.minDateMillis,
                    initialDate = state.initialDate
                )
                is PetDetailsEffect.ShowErrorDialog -> showErrorDialog(state.message)
                is PetDetailsEffect.ShowDeletePetConfirmation ->
                showConfirmationDialog(
                    message = state.message,
                    positiveAction = {
                        petDetailsViewModel.reduce(event = PetDetailsEvent.OnConfirmDeletePetClicked(args.id))
                    }
                )
            }
        }

        petDetailsViewModel.dateState.observeNotSuspend(viewLifecycleOwner) { state ->
            viewBinding.apply {
                editTextBirthDate.setText(state.date)
            }
        }

        petDetailsViewModel.petDetailsState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PetDetailsState.Loading -> {
                    showLoading(true)
                }
                is PetDetailsState.Success -> {
                    showLoading(false)
                    loadPetData(state.pet)
                }

                is PetDetailsState.Error -> {
                    showLoading(false)
                }
            }
        }

        petDetailsViewModel.deletePetState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is DeletePetState.Initial -> showLoading(false)

                is DeletePetState.Loading -> {
                    showLoading(true)
                }

                is DeletePetState.Success -> {
                    showLoading(false)
                }

                is DeletePetState.Error -> {
                    showLoading(false)
                }
            }
        }

        petDetailsViewModel.updatePetState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UpdatePetState.Initial -> {
                    showUpdateLoading(false)
                    hideFieldError()
                }
                is UpdatePetState.Loading -> {
                    hideFieldError()
                    showUpdateLoading(true)
                }
                is UpdatePetState.Success -> {
                    showUpdateLoading(false)
                    hideFieldError()
                    showEditMode(false)
                }

                is UpdatePetState.Error.FieldError -> {
                    showUpdateLoading(false)
                    showFieldError(state.message)
                }
                is UpdatePetState.Error.GlobalError -> {
                    showUpdateLoading(false)
                }
            }
        }
    }

    private fun showEditMode(show: Boolean) {
        viewBinding.apply {
            buttonEdit.isVisible = !show
            buttonDeletePet.isVisible = !show

            buttonSave.isVisible = show
            buttonCancel.isVisible = show

            textInputLayoutName.isEnabled = show
            textInputLayoutBirthDate.isEnabled = show

            textViewLabelSpecies.isVisible = !show
            textInputLayoutSpecies.isVisible = !show

            if (editTextSpecies.text.toString() == getString(ru.fav.petcare.presentation.R.string.dog)) {
                textViewLabelBreed.isVisible = !show
                textInputLayoutBreed.isVisible = !show
            }

//            textViewLabelSpecies.alpha = if (show) 0.6F else 0.8F
//            textViewLabelBreed.alpha = if (show) 0.6F else 0.8F
//            editTextSpecies.alpha = if (show) 0.6F else 1F
//            editTextBreed.alpha = if (show) 0.6F else 1F
        }
    }

    private fun loadPetData(pet: PetModel) = with(viewBinding) {
        this.apply {
            editTextName.setText(pet.name)
            editTextSpecies.setText(pet.species)
            editTextBirthDate.setText(pet.birthDate)

            if (pet.species == getString(ru.fav.petcare.presentation.R.string.dog)) {
                textViewLabelBreed.visibility = View.VISIBLE
                textInputLayoutBreed.visibility = View.VISIBLE
                editTextBreed.setText(pet.breed)
            }

            val placeholder =
                if (pet.species == getString(ru.fav.petcare.presentation.R.string.cat)) {
                    ru.fav.petcare.presentation.R.drawable.ic_cat
                } else if (pet.species == getString(ru.fav.petcare.presentation.R.string.dog)) {
                    ru.fav.petcare.presentation.R.drawable.ic_dog
                } else {
                    null
                }

            Glide.with(this@PetDetailsFragment)
                .load(pet.imagePath)
                .error(placeholder)
                .into(imageViewPetPhoto)
        }
    }

    private fun showUpdateLoading(isLoading: Boolean) {
        viewBinding.buttonSave.isEnabled = !isLoading
        viewBinding.buttonCancel.isEnabled = !isLoading
    }

    private fun showDatePicker(maxDateMillis: Long, minDateMillis: Long, initialDate: Calendar) {
        DatePickerDialog(
            requireContext(),
            ru.fav.petcare.presentation.R.style.ThemeOverlay_App_DatePicker,
            { _, year, month, day ->
                val selectedDate = Calendar.getInstance().apply { set(year, month, day) }
                petDetailsViewModel.reduce(PetDetailsEvent.OnDateSelected(selectedDate))
            },
            initialDate.get(Calendar.YEAR),
            initialDate.get(Calendar.MONTH),
            initialDate.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.maxDate = maxDateMillis
            datePicker.minDate = minDateMillis
            setButton(DialogInterface.BUTTON_NEGATIVE, getString(ru.fav.petcare.presentation.R.string.cancel)) { _, _ ->
                dismiss()
            }

            show()
        }
    }

    private fun showFieldError(message: String) {
        viewBinding.textError.apply {
            text = message
            visibility = View.VISIBLE
        }
    }

    private fun hideFieldError() {
        viewBinding.textError.visibility = View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        viewBinding.buttonEdit.isEnabled = !isLoading
        viewBinding.buttonDeletePet.isEnabled = !isLoading
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}