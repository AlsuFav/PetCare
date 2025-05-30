package ru.fav.petcare.appointment.all.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import ru.fav.petcare.appointment.all.databinding.FragmentAllAppointmentsBinding
import ru.fav.petcare.appointment.all.R
import ru.fav.petcare.appointment.all.ui.adapter.AppointmentsAdapter
import ru.fav.petcare.appointment.all.ui.adapter.AppointmentsShimmerAdapter
import ru.fav.petcare.appointment.all.ui.state.AllAppointmentsEffect
import ru.fav.petcare.appointment.all.ui.state.AllAppointmentsEvent
import ru.fav.petcare.appointment.all.ui.state.AllAppointmentsState
import ru.fav.petcare.domain.model.AppointmentModel
import ru.fav.petcare.util.extension.observe
import ru.fav.petcare.util.extension.observeNotSuspend
import ru.fav.petcare.util.extension.showErrorDialog
import kotlin.getValue

@AndroidEntryPoint
class AllAppointmentsFragment: Fragment(R.layout.fragment_all_appointments) {

    private val viewBinding: FragmentAllAppointmentsBinding by viewBinding(FragmentAllAppointmentsBinding::bind)
    private var rvAdapter: AppointmentsAdapter? = null
    private var rvShimmerAdapter: AppointmentsShimmerAdapter? = null

    private val allAppointmentsViewModel: AllAppointmentsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeViewModel()

        allAppointmentsViewModel.reduce(event = AllAppointmentsEvent.GetAllUpcomingAppointments)
    }

    private fun initViews() = with(viewBinding) {
        setupRecyclerViews()

        this.buttonAddAppointment.setOnClickListener {
            allAppointmentsViewModel.reduce(AllAppointmentsEvent.OnAddAppointmentClicked)
        }

        this.buttonUpcomingAppointment.setOnClickListener {
            showAppointmentMode(upcoming = true)
            allAppointmentsViewModel.reduce(event = AllAppointmentsEvent.GetAllUpcomingAppointments)
        }

        this.buttonPassedAppointment.setOnClickListener {
            showAppointmentMode(upcoming = false)
            allAppointmentsViewModel.reduce(event = AllAppointmentsEvent.GetAllPassedAppointments)
        }
    }

    private fun observeViewModel() {
        allAppointmentsViewModel.effect.observeNotSuspend(viewLifecycleOwner) { state ->
            when (state) {
                is AllAppointmentsEffect.ShowErrorDialog -> showErrorDialog(state.message)
            }
        }

        allAppointmentsViewModel.allAppointmentsState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AllAppointmentsState.Loading -> {
                    hideErrorField()
                    showLoading(true)
                }
                is AllAppointmentsState.Success -> {
                    showLoading(false)
                    hideErrorField()
                    rvAdapter?.updateData(state.appointments.toMutableList())
                }

                is AllAppointmentsState.Error.FieldError -> {
                    showLoading(false)
                    showErrorField(state.message)
                }
                is AllAppointmentsState.Error.GlobalError -> {
                    showLoading(false)
                }
            }
        }
    }

    private fun setupRecyclerViews() {
        rvAdapter = AppointmentsAdapter { appointment ->
            allAppointmentsViewModel.reduce(AllAppointmentsEvent.OnAppointmentClicked(appointment.id))
        }
        viewBinding.recyclerViewAppointments.adapter = rvAdapter

        rvShimmerAdapter = AppointmentsShimmerAdapter()
        viewBinding.recyclerViewShimmer.adapter = rvShimmerAdapter
    }

    private fun showAppointmentMode(upcoming: Boolean) {
        viewBinding.apply {
            buttonUpcomingAppointment.isVisible = !upcoming
            buttonPassedAppointment.isVisible = upcoming

            textViewAppointmentList.text.apply {
                if (upcoming) {
                    getString(R.string.upcoming_appointment_list)
                } else {
                    getString(R.string.passed_appointment_list)
                }
            }

        }
    }

    private fun showLoading(isLoading: Boolean) {
        viewBinding.apply {
            if (isLoading) {
                shimmerLayout.visibility = View.VISIBLE
                shimmerLayout.startShimmer()
                recyclerViewAppointments.visibility = View.GONE
            } else {
                shimmerLayout.visibility = View.GONE
                shimmerLayout.stopShimmer()
                recyclerViewAppointments.visibility = View.VISIBLE
            }
        }
    }

    private fun showErrorField(message: String) {
        viewBinding.textError.apply {
            text = message
            visibility = View.VISIBLE
        }
        viewBinding.recyclerViewAppointments.visibility = View.GONE
    }

    private fun hideErrorField() {
        viewBinding.textError.visibility = View.GONE
        viewBinding.recyclerViewAppointments.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding.shimmerLayout.stopShimmer()
    }
}