package ru.fav.petcare.profile.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import ru.fav.petcare.domain.model.ClientModel
import ru.fav.petcare.profile.databinding.FragmentProfileBinding
import ru.fav.petcare.presentation.util.watchers.PhoneNumberTextWatcher
import ru.fav.petcare.profile.R
import ru.fav.petcare.profile.ui.state.ClearJwtState
import ru.fav.petcare.profile.ui.state.ProfileEffect
import ru.fav.petcare.profile.ui.state.ProfileEvent
import ru.fav.petcare.profile.ui.state.ProfileState
import ru.fav.petcare.profile.ui.state.UpdateProfileState
import ru.fav.petcare.util.extension.observe
import ru.fav.petcare.util.extension.observeNotSuspend
import ru.fav.petcare.util.extension.showErrorDialog
import kotlin.getValue

@AndroidEntryPoint
class ProfileFragment: Fragment(R.layout.fragment_profile) {

    private val viewBinding: FragmentProfileBinding by viewBinding(FragmentProfileBinding::bind)

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()

        profileViewModel.reduce(event = ProfileEvent.GetClientData)
    }

    private fun initViews() = with(viewBinding) {
        this.editTextPhone.addTextChangedListener(PhoneNumberTextWatcher(this.editTextPhone))

        this.buttonEdit.setOnClickListener {
            showEditMode(true)
        }

        this.buttonSave.setOnClickListener {
            val firstName = viewBinding.editTextFirstName.text.toString().trim()
            val lastName = viewBinding.editTextLastName.text.toString().trim()
            val phone = viewBinding.editTextPhone.text.toString().trim()

            profileViewModel.reduce(event = ProfileEvent.OnSaveClicked(
                firstName = firstName,
                lastName = lastName,
                phone = phone
            ))
        }

        this.buttonCancel.setOnClickListener {
            hideFieldError()
            showEditMode(false)
            profileViewModel.reduce(event = ProfileEvent.GetClientData)
        }

        this.buttonSafety.setOnClickListener {
            profileViewModel.reduce(event = ProfileEvent.OnSafetyClicked)
        }

        this.buttonLogOut.setOnClickListener {
            profileViewModel.reduce(event = ProfileEvent.OnLogOutClicked)
        }
    }

    private fun observeViewModel() {
        profileViewModel.effect.observeNotSuspend(viewLifecycleOwner) { state ->
            when (state) {
                is ProfileEffect.ShowToast -> showToast(state.message)
            }
        }

        profileViewModel.profileState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProfileState.Loading -> {
                    showLoading(true)
                }
                is ProfileState.Success -> {
                    showLoading(false)
                    loadClient(state.client)
                }

                is ProfileState.Error -> {
                    showLoading(false)

                    showErrorDialog(
                        message = state.message
                    )
                }
            }
        }

        profileViewModel.updateProfileState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UpdateProfileState.Initial -> {
                    showUpdateLoading(false)
                    hideFieldError()
                }
                is UpdateProfileState.Loading -> {
                    hideFieldError()
                    showUpdateLoading(true)
                }
                is UpdateProfileState.Success -> {
                    showUpdateLoading(false)
                    hideFieldError()
                    showEditMode(false)
                }

                is UpdateProfileState.Error.FieldError -> {
                    showUpdateLoading(false)
                    showFieldError(state.message)
                }
                is UpdateProfileState.Error.GlobalError -> {
                    showUpdateLoading(false)

                    showErrorDialog(
                        message = state.message
                    )
                }
            }
        }

        profileViewModel.clearJwtState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ClearJwtState.Initial -> {
                    showLoading(false)
                }
                is ClearJwtState.Loading -> {
                    showLoading(true)
                }
                is ClearJwtState.Success -> {
                    showLoading(false)
                }

                is ClearJwtState.Error -> {
                    showLoading(false)

                    showErrorDialog(
                        message = state.message
                    )
                }
            }
        }
    }

    private fun showEditMode(show: Boolean) {
        viewBinding.apply {
            buttonEdit.isVisible = !show
            buttonSafety.isVisible = !show
            buttonLogOut.isVisible = !show

            buttonSave.isVisible = show
            buttonCancel.isVisible = show

            textInputLayoutFirstName.isEnabled = show
            textInputLayoutLastName.isEnabled = show
            textInputLayoutPhone.isEnabled = show
        }
    }

    private fun loadClient(client: ClientModel) = with(viewBinding) {
        this.apply {
            editTextFirstName.setText(client.firstName)
            editTextLastName.setText(client.lastName)
            editTextPhone.setText(client.phone)
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

    private fun showLoading(isLoading: Boolean) {
        viewBinding.buttonLogOut.isEnabled = !isLoading
        viewBinding.buttonEdit.isEnabled = !isLoading
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
