package ru.fav.petcare.appointment.add.pet.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import ru.fav.petcare.appointment.add.pet.databinding.FragmentSelectPetBinding
import ru.fav.petcare.appointment.add.pet.R
import ru.fav.petcare.appointment.add.pet.ui.adapter.PetsAdapter
import ru.fav.petcare.appointment.add.pet.ui.adapter.PetsShimmerAdapter
import ru.fav.petcare.appointment.add.pet.ui.state.SelectPetEffect
import ru.fav.petcare.appointment.add.pet.ui.state.SelectPetEvent
import ru.fav.petcare.appointment.add.pet.ui.state.SelectPetState
import ru.fav.petcare.util.extension.observe
import ru.fav.petcare.util.extension.observeNotSuspend
import ru.fav.petcare.util.extension.showErrorDialog
import kotlin.getValue

@AndroidEntryPoint
class SelectPetFragment: Fragment(R.layout.fragment_select_pet) {

    private val viewBinding: FragmentSelectPetBinding by viewBinding(FragmentSelectPetBinding::bind)
    private val selectPetViewModel: SelectPetViewModel by viewModels()

    private var rvAdapter: PetsAdapter? = null
    private var rvShimmerAdapter: PetsShimmerAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeViewModel()

        selectPetViewModel.reduce(event = SelectPetEvent.GetAllPets)
    }

    private fun initViews() = with(viewBinding) {
        setupRecyclerViews()

        this.buttonCancel.setOnClickListener {
            selectPetViewModel.reduce(SelectPetEvent.OnCancelClicked)
        }

        this.buttonAddPet.setOnClickListener {
            selectPetViewModel.reduce(SelectPetEvent.OnAddPetClicked)
        }
    }

    private fun observeViewModel() {
        selectPetViewModel.effect.observeNotSuspend(viewLifecycleOwner) { state ->
            when (state) {
                is SelectPetEffect.ShowErrorDialog -> showErrorDialog(state.message)
            }
        }

        selectPetViewModel.selectPetState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SelectPetState.Loading -> {
                    hideErrorField()
                    showLoading(true)
                }
                is SelectPetState.Success -> {
                    showLoading(false)
                    hideErrorField()
                    rvAdapter?.updateData(state.pets.toMutableList())
                }

                is SelectPetState.Error.FieldError -> {
                    showLoading(false)
                    showErrorField(state.message)
                }
                is SelectPetState.Error.GlobalError -> {
                    showLoading(false)
                }
            }
        }
    }

    private fun setupRecyclerViews() {
        rvAdapter = PetsAdapter { pet ->
            selectPetViewModel.reduce(SelectPetEvent.OnPetClicked(pet.id))
        }
        viewBinding.recyclerViewPets.adapter = rvAdapter

        rvShimmerAdapter = PetsShimmerAdapter()
        viewBinding.recyclerViewShimmer.adapter = rvShimmerAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        viewBinding.apply {
            if (isLoading) {
                shimmerLayout.visibility = View.VISIBLE
                shimmerLayout.startShimmer()
                recyclerViewPets.visibility = View.GONE
            } else {
                shimmerLayout.visibility = View.GONE
                shimmerLayout.stopShimmer()
                recyclerViewPets.visibility = View.VISIBLE
            }
        }
    }

    private fun showErrorField(message: String) {
        viewBinding.textError.apply {
            text = message
            visibility = View.VISIBLE
        }

        viewBinding.textViewSelectPet.visibility = View.GONE
        viewBinding.buttonAddPet.visibility = View.VISIBLE
    }

    private fun hideErrorField() {
        viewBinding.textError.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding.shimmerLayout.stopShimmer()
    }
}