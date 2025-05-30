package ru.fav.petcare.appointment.details.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import ru.fav.petcare.appointment.details.R
import ru.fav.petcare.appointment.details.databinding.FragmentAppointmentDetailsBinding
import ru.fav.petcare.appointment.details.ui.state.AppointmentDetailsEffect
import ru.fav.petcare.appointment.details.ui.state.AppointmentDetailsEvent
import ru.fav.petcare.appointment.details.ui.state.AppointmentDetailsState
import ru.fav.petcare.appointment.details.ui.state.CancelAppointmentState
import ru.fav.petcare.domain.model.AppointmentModel
import ru.fav.petcare.util.extension.observe
import ru.fav.petcare.util.extension.observeNotSuspend
import ru.fav.petcare.util.extension.showConfirmationDialog
import ru.fav.petcare.util.extension.showErrorDialog
import kotlin.getValue

@AndroidEntryPoint
class AppointmentDetailsFragment: Fragment(R.layout.fragment_appointment_details) {

    private val viewBinding: FragmentAppointmentDetailsBinding by viewBinding(FragmentAppointmentDetailsBinding::bind)

    private val appointmentDetailsViewModel: AppointmentDetailsViewModel by viewModels()

    private val args: AppointmentDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()

        appointmentDetailsViewModel.reduce(event = AppointmentDetailsEvent.GetAppointmentData(args.id))
    }

    private fun initViews() = with(viewBinding) {

        this.buttonBack.setOnClickListener {
            appointmentDetailsViewModel.reduce(event = AppointmentDetailsEvent.OnBackClicked)
        }

        this.buttonCancelAppointment.setOnClickListener {
            appointmentDetailsViewModel.reduce(event = AppointmentDetailsEvent.OnCancelAppointmentClicked)
        }
    }

    private fun observeViewModel() {
        appointmentDetailsViewModel.effect.observeNotSuspend(viewLifecycleOwner) { state ->
            when (state) {
                is AppointmentDetailsEffect.ShowToast -> showToast(state.message)
                is AppointmentDetailsEffect.ShowErrorDialog -> showErrorDialog(state.message)
                is AppointmentDetailsEffect.ShowCancelAppointmentConfirmation ->
                    showConfirmationDialog(
                        message = state.message,
                        positiveAction = {
                            appointmentDetailsViewModel.reduce(
                                event = AppointmentDetailsEvent.OnConfirmCancelAppointmentClicked(args.id)
                            )
                        }
                    )
            }
        }

        appointmentDetailsViewModel.appointmentDetailsState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AppointmentDetailsState.Loading -> {
                    showLoading(true)
                }
                is AppointmentDetailsState.Success -> {
                    showLoading(false)
                    loadAppointmentData(state.appointment)

                    if (state.appointment.upcoming) {
                        viewBinding.buttonCancelAppointment.visibility = View.VISIBLE
                    }
                }

                is AppointmentDetailsState.Error -> {
                    showLoading(false)
                }
            }
        }

        appointmentDetailsViewModel.cancelAppointmentState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CancelAppointmentState.Initial -> showLoading(false)

                is CancelAppointmentState.Loading -> {
                    showLoading(true)
                }

                is CancelAppointmentState.Success -> {
                    showLoading(false)
                }

                is CancelAppointmentState.Error -> {
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
        viewBinding.buttonCancelAppointment.isEnabled = !isLoading
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}