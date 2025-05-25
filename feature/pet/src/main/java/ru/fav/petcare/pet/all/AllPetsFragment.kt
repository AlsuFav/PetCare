package ru.fav.petcare.pet.all

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import ru.fav.petcare.pet.R
import ru.fav.petcare.pet.all.adapter.PetsAdapter
import ru.fav.petcare.pet.all.adapter.PetsShimmerAdapter
import ru.fav.petcare.pet.all.state.AllPetsEffect
import ru.fav.petcare.pet.all.state.AllPetsEvent
import ru.fav.petcare.pet.all.state.AllPetsState
import ru.fav.petcare.pet.databinding.FragmentAllPetsBinding
import ru.fav.petcare.util.extension.observe
import ru.fav.petcare.util.extension.observeNotSuspend
import ru.fav.petcare.util.extension.showErrorDialog
import java.util.Calendar
import kotlin.getValue

@AndroidEntryPoint
class AllPetsFragment: Fragment(R.layout.fragment_all_pets) {

    private val viewBinding: FragmentAllPetsBinding by viewBinding(FragmentAllPetsBinding::bind)
    private var rvAdapter: PetsAdapter? = null
    private var rvShimmerAdapter: PetsShimmerAdapter? = null

    private val allPetsViewModel: AllPetsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeViewModel()

        allPetsViewModel.reduce(event = AllPetsEvent.GetAllPets)
    }

    private fun initViews() = with(viewBinding) {
        setupRecyclerViews()

        this.buttonAddPet.setOnClickListener {
            allPetsViewModel.reduce(AllPetsEvent.OnAddPetClicked)
        }
    }

    private fun observeViewModel() {
        allPetsViewModel.effect.observeNotSuspend(viewLifecycleOwner) { state ->
            when (state) {
                is AllPetsEffect.ShowErrorDialog -> showErrorDialog(state.message)
            }
        }

        allPetsViewModel.allPetsState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AllPetsState.Loading -> {
                    hideErrorField()
                    showLoading(true)
                }
                is AllPetsState.Success -> {
                    showLoading(false)
                    hideErrorField()
                    rvAdapter?.updateData(state.pets.toMutableList())
                }

                is AllPetsState.Error.FieldError -> {
                    showLoading(false)
                    showErrorField(state.message)
                }
                is AllPetsState.Error.GlobalError -> {
                    showLoading(false)
                }
            }
        }
    }

    private fun setupRecyclerViews() {
        rvAdapter = PetsAdapter { pet ->
            allPetsViewModel.reduce(AllPetsEvent.OnPetClicked(pet.id))
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
    }

    private fun hideErrorField() {
        viewBinding.textError.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding.shimmerLayout.stopShimmer()
    }
}