package ru.fav.petcare.pet.add

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import ru.fav.petcare.pet.databinding.FragmentAddPetBinding
import ru.fav.petcare.pet.R
import ru.fav.petcare.pet.add.state.AddPetEffect
import ru.fav.petcare.pet.add.state.AddPetEvent
import ru.fav.petcare.pet.add.state.AddPetState
import ru.fav.petcare.util.extension.observe
import ru.fav.petcare.util.extension.dp
import ru.fav.petcare.util.extension.observeNotSuspend
import ru.fav.petcare.util.extension.showErrorDialog
import java.util.Calendar
import kotlin.getValue

@AndroidEntryPoint
class AddPetFragment: Fragment(R.layout.fragment_add_pet) {

    private val viewBinding: FragmentAddPetBinding by viewBinding(FragmentAddPetBinding::bind)
    private val addPetViewModel: AddPetViewModel by viewModels()

    private var isDogSelected = false
    private var breedAdapter: ArrayAdapter<String>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeViewModel()

    }

    private fun initViews() = with(viewBinding) {
        setupBreedAutoComplete()

        viewBinding.cardCat.setOnClickListener {
            isDogSelected = false
            highlightSelectedSpecies(true)
            textInputLayoutBreed.visibility = View.GONE
            breedAutoComplete.setText("")
        }

        viewBinding.cardDog.setOnClickListener {
            isDogSelected = true
            highlightSelectedSpecies(false)
            textInputLayoutBreed.visibility = View.VISIBLE
        }


        this.editTextBirthDate.setOnClickListener {
            addPetViewModel.reduce(AddPetEvent.OnDateClicked)
        }

        this.buttonAddPet.setOnClickListener {
            val name = this.editTextName.text.toString()
            val species = if (isDogSelected) getString(R.string.dog) else getString(R.string.cat)
            val breed = this.breedAutoComplete.text.toString()
            val birthDate = this.editTextBirthDate.text.toString()
            addPetViewModel.reduce(AddPetEvent.OnAddPetClicked(
                name = name,
                species = species,
                breed = breed,
                birthDate = birthDate
            ))
        }

        this.buttonCancel.setOnClickListener {
            addPetViewModel.reduce(AddPetEvent.OnCancelClicked)
        }
    }

    private fun setupBreedAutoComplete() {
        breedAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line)
        viewBinding.breedAutoComplete.setAdapter(breedAdapter)
        viewBinding.breedAutoComplete.threshold = 0

        viewBinding.breedAutoComplete.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        viewBinding.breedAutoComplete.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewBinding.breedAutoComplete.showDropDown()
            }
        }

        addPetViewModel.reduce(AddPetEvent.LoadBreeds)
    }


    private fun observeViewModel() {
        addPetViewModel.dateState.observeNotSuspend(viewLifecycleOwner) { state ->
            viewBinding.apply {
                editTextBirthDate.setText(state.date)
            }
        }

        addPetViewModel.breedsState.observe(viewLifecycleOwner) { state ->
            breedAdapter?.addAll(state.breeds)
        }

        addPetViewModel.effect.observeNotSuspend(viewLifecycleOwner) { state ->
            when (state) {
                is AddPetEffect.ShowErrorDialog -> showErrorDialog(state.message)
                is AddPetEffect.ShowDatePicker -> showDatePicker(
                    maxDateMillis = state.maxDateMillis,
                    minDateMillis = state.minDateMillis,
                    initialDate = state.initialDate
                )
                is AddPetEffect.ShowToast -> showToast(state.message)
            }
        }

        addPetViewModel.addPetState.observe(viewLifecycleOwner) { state ->
            when (state) {
                AddPetState.Initial -> {
                    hideErrorField()
                    showLoading(false)
                }
                is AddPetState.Loading -> {
                    hideErrorField()
                    showLoading(true)
                }
                is AddPetState.Success -> {
                    showLoading(false)
                    hideErrorField()
                }
                is AddPetState.Error.FieldError -> {
                    showLoading(false)
                    showErrorField(state.message)
                }
                is AddPetState.Error.GlobalError -> {
                    showLoading(false)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        viewBinding.buttonAddPet.isEnabled = !isLoading
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showDatePicker(maxDateMillis: Long, minDateMillis: Long, initialDate: Calendar) {
        DatePickerDialog(
            requireContext(),
            ru.fav.petcare.presentation.R.style.ThemeOverlay_App_DatePicker,
            { _, year, month, day ->
                val selectedDate = Calendar.getInstance().apply { set(year, month, day) }
                addPetViewModel.reduce(AddPetEvent.OnDateSelected(selectedDate))
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

    private fun showErrorField(message: String) {
        viewBinding.textError.apply {
            text = message
            visibility = View.VISIBLE
        }
    }

    private fun hideErrorField() {
        viewBinding.textError.visibility = View.GONE
    }

    private fun highlightSelectedSpecies(isCat: Boolean) = with(viewBinding) {
        cardCat.cardElevation = if (isCat) 8f else 2f
        cardDog.cardElevation = if (!isCat) 8f else 2f

        cardCat.strokeWidth = if (isCat) 3f.dp else 1f.dp
        cardDog.strokeWidth = if (!isCat) 3f.dp else 1f.dp
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}