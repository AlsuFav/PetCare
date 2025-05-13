package ru.fav.petcare.safety.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import ru.fav.petcare.presentation.util.ErrorDialogUtil
import ru.fav.petcare.safety.R
import ru.fav.petcare.safety.databinding.FragmentSafetyBinding
import ru.fav.petcare.safety.ui.state.DeleteClientState
import ru.fav.petcare.safety.ui.state.SafetyEffect
import ru.fav.petcare.safety.ui.state.SafetyEvent
import ru.fav.petcare.safety.ui.state.UpdatePasswordState
import ru.fav.petcare.util.extension.observe
import ru.fav.petcare.util.extension.observeNotSuspend
import kotlin.getValue

@AndroidEntryPoint
class SafetyFragment: Fragment(R.layout.fragment_safety) {

    private val viewBinding: FragmentSafetyBinding by viewBinding(FragmentSafetyBinding::bind)

    private val safetyViewModel: SafetyViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModelState()
    }

    private fun initViews() = with(viewBinding) {
        this.buttonBack.setOnClickListener {
            safetyViewModel.reduce(event = SafetyEvent.OnBackClicked)
        }

        this.buttonChangePassword.setOnClickListener {
            showEditMode(true)
        }

        this.buttonSave.setOnClickListener {
            val currentPassword = viewBinding.editTextPassword.text.toString().trim()
            val newPassword = viewBinding.editTextNewPassword.text.toString().trim()
            val confirmNewPassword = viewBinding.editTextConfirmNewPassword.text.toString().trim()

            safetyViewModel.reduce(event = SafetyEvent.OnChangePasswordClicked(
                currentPassword = currentPassword,
                newPassword = newPassword,
                confirmNewPassword = confirmNewPassword
            ))
        }

        this.buttonCancel.setOnClickListener {
            hideFieldError()
            cleanFields()
            showEditMode(false)
        }

        this.buttonDeleteAccount.setOnClickListener {
            safetyViewModel.reduce(event = SafetyEvent.OnDeleteAccountClicked)
        }
    }

    private fun observeViewModelState() {
        safetyViewModel.effect.observeNotSuspend(viewLifecycleOwner) { state ->
            when (state) {
                is SafetyEffect.ShowToast -> showToast(state.message)
                is SafetyEffect.ShowDeleteAccountConfirmation ->
                    ErrorDialogUtil.showConfirmationDialog(
                    context = requireContext(),
                    message = state.message,
                    positiveAction = {
                        safetyViewModel.reduce(event = SafetyEvent.OnConfirmDeleteAccountClicked)
                    }
                )
            }
        }

        safetyViewModel.updatePasswordState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UpdatePasswordState.Initial -> {
                    showUpdateLoading(false)
                    hideFieldError()
                }
                is UpdatePasswordState.Loading -> {
                    hideFieldError()
                    showUpdateLoading(true)
                }
                is UpdatePasswordState.Success -> {
                    showUpdateLoading(false)
                    hideFieldError()
                    cleanFields()
                    showEditMode(false)
                }

                is UpdatePasswordState.Error.FieldError -> {
                    showUpdateLoading(false)
                    showFieldError(state.message)
                }
                is UpdatePasswordState.Error.GlobalError -> {
                    showUpdateLoading(false)

                    ErrorDialogUtil.showErrorDialog(
                        context = requireContext(),
                        message = state.message
                    )
                }
            }
        }

        safetyViewModel.deleteClientState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is DeleteClientState.Initial -> {
                    showDeleteAccountButtonLoading(false)
                }
                is DeleteClientState.Loading -> {
                    showDeleteAccountButtonLoading(true)
                }
                is DeleteClientState.Success -> {
                    showDeleteAccountButtonLoading(false)
                }

                is DeleteClientState.Error -> {
                    showDeleteAccountButtonLoading(false)

                    ErrorDialogUtil.showErrorDialog(
                        context = requireContext(),
                        message = state.message
                    )
                }
            }
        }
    }

    private fun showEditMode(show: Boolean) {
        viewBinding.apply {
            buttonChangePassword.isVisible = !show
            buttonDeleteAccount.isVisible = !show
            buttonBack.isVisible = !show

            buttonSave.isVisible = show
            buttonCancel.isVisible = show

            textInputLayoutPassword.isVisible = show
            textInputLayoutNewPassword.isVisible = show
            textInputLayoutConfirmNewPassword.isVisible = show
        }
    }

    private fun cleanFields() {
        viewBinding.apply {
            editTextPassword.setText("")
            editTextNewPassword.setText("")
            editTextConfirmNewPassword.setText("")
        }
    }

    private fun showUpdateLoading(isLoading: Boolean) {
        viewBinding.buttonSave.isEnabled = !isLoading
        viewBinding.buttonCancel.isEnabled = !isLoading
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

    private fun showDeleteAccountButtonLoading(isLoading: Boolean) {
        viewBinding.buttonDeleteAccount.isEnabled = !isLoading
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
