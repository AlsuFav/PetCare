package ru.fav.petcare.appointment.add.service.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import ru.fav.petcare.appointment.add.service.databinding.FragmentSelectServiceBinding
import ru.fav.petcare.appointment.add.service.R
import ru.fav.petcare.appointment.add.service.ui.adapter.ServicesAdapter
import ru.fav.petcare.appointment.add.service.ui.adapter.ServicesShimmerAdapter
import ru.fav.petcare.appointment.add.service.ui.state.SelectServiceEffect
import ru.fav.petcare.appointment.add.service.ui.state.SelectServiceEvent
import ru.fav.petcare.appointment.add.service.ui.state.SelectServiceState
import ru.fav.petcare.util.extension.observe
import ru.fav.petcare.util.extension.observeNotSuspend
import ru.fav.petcare.util.extension.showErrorDialog
import kotlin.getValue

@AndroidEntryPoint
class SelectServiceFragment: Fragment(R.layout.fragment_select_service) {

    private val viewBinding: FragmentSelectServiceBinding by viewBinding(FragmentSelectServiceBinding::bind)
    private val args: SelectServiceFragmentArgs by navArgs()
    private val selectServiceViewModel: SelectServiceViewModel by viewModels()

    private var rvAdapter: ServicesAdapter? = null
    private var rvShimmerAdapter: ServicesShimmerAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeViewModel()

        selectServiceViewModel.reduce(event = SelectServiceEvent.GetAllServicesForPet(args.petId))
    }

    private fun initViews() = with(viewBinding) {
        setupRecyclerViews()

        this.buttonCancel.setOnClickListener {
            selectServiceViewModel.reduce(SelectServiceEvent.OnCancelClicked)
        }

        this.buttonBack.setOnClickListener {
            selectServiceViewModel.reduce(SelectServiceEvent.OnBackClicked)
        }
    }

    private fun observeViewModel() {
        selectServiceViewModel.effect.observeNotSuspend(viewLifecycleOwner) { state ->
            when (state) {
                is SelectServiceEffect.ShowErrorDialog -> showErrorDialog(state.message)
            }
        }

        selectServiceViewModel.selectServiceState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SelectServiceState.Loading -> {
                    hideErrorField()
                    showLoading(true)
                }
                is SelectServiceState.Success -> {
                    showLoading(false)
                    hideErrorField()
                    rvAdapter?.updateData(state.services.toMutableList())
                }

                is SelectServiceState.Error.FieldError -> {
                    showLoading(false)
                    showErrorField(state.message)
                }
                is SelectServiceState.Error.GlobalError -> {
                    showLoading(false)
                }
            }
        }
    }

    private fun setupRecyclerViews() {
        rvAdapter = ServicesAdapter { service ->
            selectServiceViewModel.reduce(SelectServiceEvent.OnServiceClicked(args.petId, service.id))
        }
        viewBinding.recyclerViewServices.adapter = rvAdapter

        rvShimmerAdapter = ServicesShimmerAdapter()
        viewBinding.recyclerViewShimmer.adapter = rvShimmerAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        viewBinding.apply {
            if (isLoading) {
                shimmerLayout.visibility = View.VISIBLE
                shimmerLayout.startShimmer()
                recyclerViewServices.visibility = View.GONE
            } else {
                shimmerLayout.visibility = View.GONE
                shimmerLayout.stopShimmer()
                recyclerViewServices.visibility = View.VISIBLE
            }
        }
    }

    private fun showErrorField(message: String) {
        viewBinding.textError.apply {
            text = message
            visibility = View.VISIBLE
        }

        viewBinding.textViewSelectService.visibility = View.GONE
    }

    private fun hideErrorField() {
        viewBinding.textError.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding.shimmerLayout.stopShimmer()
    }
}