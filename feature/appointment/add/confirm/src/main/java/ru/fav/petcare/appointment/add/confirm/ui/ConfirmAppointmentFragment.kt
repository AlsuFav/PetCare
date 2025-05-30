package ru.fav.petcare.appointment.add.confirm.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import ru.fav.petcare.appointment.add.confirm.databinding.FragmentConfirmAppointmentBinding
import ru.fav.petcare.appointment.add.confirm.R
import ru.fav.petcare.appointment.add.confirm.ui.state.AppointmentDetailsState
import ru.fav.petcare.appointment.add.confirm.ui.state.ConfirmAppointmentEffect
import ru.fav.petcare.appointment.add.confirm.ui.state.ConfirmAppointmentEvent
import ru.fav.petcare.appointment.add.confirm.ui.state.ConfirmAppointmentState
import ru.fav.petcare.domain.model.AppointmentModel
import ru.fav.petcare.util.extension.observe
import ru.fav.petcare.util.extension.observeNotSuspend
import ru.fav.petcare.util.extension.showErrorDialog
import kotlin.getValue

@AndroidEntryPoint
class ConfirmAppointmentFragment: Fragment(R.layout.fragment_confirm_appointment) {

    private val viewBinding: FragmentConfirmAppointmentBinding by viewBinding(
        FragmentConfirmAppointmentBinding::bind)
    private val args: ConfirmAppointmentFragmentArgs by navArgs()
    private val confirmAppointmentViewModel: ConfirmAppointmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeViewModel()

        confirmAppointmentViewModel.reduce(event = ConfirmAppointmentEvent.GetConfirmAppointmentData(
            petId = args.petId,
            serviceId = args.serviceId,
            timeSlotId = args.timeSlotId
        ))
    }

    private fun initViews() = with(viewBinding) {

        this.buttonCancel.setOnClickListener {
            confirmAppointmentViewModel.reduce(ConfirmAppointmentEvent.OnCancelClicked)
        }

        this.buttonBack.setOnClickListener {
            confirmAppointmentViewModel.reduce(ConfirmAppointmentEvent.OnBackClicked)
        }

        this.buttonConfirmAppointment.setOnClickListener {
            confirmAppointmentViewModel.reduce(ConfirmAppointmentEvent.OnConfirmClicked(
                petId = args.petId,
                serviceId = args.serviceId,
                timeSlotId = args.timeSlotId
            ))
        }
    }

    private fun observeViewModel() {
        confirmAppointmentViewModel.effect.observeNotSuspend(viewLifecycleOwner) { state ->
            when (state) {
                is ConfirmAppointmentEffect.ShowToast -> showToast(state.message)
                is ConfirmAppointmentEffect.ShowErrorDialog -> showErrorDialog(state.message)
            }
        }

        confirmAppointmentViewModel.appointmentDetailsState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AppointmentDetailsState.Loading -> {
                    showLoading(true)
                }
                is AppointmentDetailsState.Success -> {
                    showLoading(false)
                    loadAppointmentData(state.appointment)
                }

                is AppointmentDetailsState.Error -> {
                    showLoading(false)
                }
            }
        }

        confirmAppointmentViewModel.confirmAppointmentState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ConfirmAppointmentState.Initial -> showLoading(false)

                is ConfirmAppointmentState.Loading -> {
                    showLoading(true)
                }

                is ConfirmAppointmentState.Success -> {
                    showLoading(false)
                }

                is ConfirmAppointmentState.Error -> {
                    showLoading(false)
                }
            }
        }
    }

    private fun loadAppointmentData(appointment: AppointmentModel) = with(viewBinding) {
        this.apply {
            editTextPetName.setText(appointment.petName)
            editTextGroomerName.setText(appointment.groomerName)
            editTextServiceName.setText(appointment.serviceName)
            editTextDate.setText(appointment.date)
            editTextPrice.setText(appointment.price.toString())
        }
    }

    private fun showLoading(isLoading: Boolean) {
        viewBinding.apply {
            viewBinding.buttonConfirmAppointment.isEnabled = !isLoading
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}