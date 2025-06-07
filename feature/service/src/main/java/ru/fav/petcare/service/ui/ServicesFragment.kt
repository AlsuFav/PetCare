package ru.fav.petcare.service.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import ru.fav.petcare.service.R
import ru.fav.petcare.service.databinding.FragmentServicesBinding
import ru.fav.petcare.service.ui.adapter.ServicesAdapter
import ru.fav.petcare.service.ui.adapter.ServicesShimmerAdapter
import ru.fav.petcare.service.ui.state.ServicesEffect
import ru.fav.petcare.service.ui.state.ServicesEvent
import ru.fav.petcare.service.ui.state.ServicesState
import ru.fav.petcare.util.extension.observe
import ru.fav.petcare.util.extension.observeNotSuspend
import ru.fav.petcare.util.extension.showErrorDialog
import kotlin.getValue

@AndroidEntryPoint
class ServicesFragment: Fragment(R.layout.fragment_services) {

    private val viewBinding: FragmentServicesBinding by viewBinding(FragmentServicesBinding::bind)
    private val servicesViewModel: ServicesViewModel by viewModels()

    private var rvAdapter: ServicesAdapter? = null
    private var rvShimmerAdapter: ServicesShimmerAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeViewModel()

        servicesViewModel.reduce(event = ServicesEvent.GetAllServices)
    }

    private fun initViews() = with(viewBinding) {
        setupRecyclerViews()

        this.buttonBack.setOnClickListener {
            servicesViewModel.reduce(ServicesEvent.OnBackClicked)
        }
    }

    private fun observeViewModel() {
        servicesViewModel.effect.observeNotSuspend(viewLifecycleOwner) { state ->
            when (state) {
                is ServicesEffect.ShowErrorDialog -> showErrorDialog(state.message)
            }
        }

        servicesViewModel.servicesState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ServicesState.Loading -> {
                    hideErrorField()
                    showLoading(true)
                }
                is ServicesState.Success -> {
                    showLoading(false)
                    hideErrorField()
                    rvAdapter?.updateData(state.services.toMutableList())
                }

                is ServicesState.Error.FieldError -> {
                    showLoading(false)
                    showErrorField(state.message)
                }
                is ServicesState.Error.GlobalError -> {
                    showLoading(false)
                }
            }
        }
    }

    private fun setupRecyclerViews() {
        rvAdapter = ServicesAdapter()
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
    }

    private fun hideErrorField() {
        viewBinding.textError.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding.shimmerLayout.stopShimmer()
    }
}