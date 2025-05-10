package ru.fav.petcare.profile.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import ru.fav.petcare.profile.databinding.FragmentProfileBinding
import ru.fav.petcare.presentation.util.ErrorDialogUtil
import ru.fav.petcare.profile.R
import ru.fav.petcare.profile.ui.state.ClearJwtState
import ru.fav.petcare.profile.ui.state.ProfileEffect
import ru.fav.petcare.profile.ui.state.ProfileEvent
import ru.fav.petcare.profile.ui.state.UpdateProfileState
import ru.fav.petcare.util.extension.observe
import ru.fav.petcare.util.extension.observeNotSuspend
import kotlin.getValue

@AndroidEntryPoint
class ProfileFragment: Fragment(R.layout.fragment_profile) {

    private val viewBinding: FragmentProfileBinding by viewBinding(FragmentProfileBinding::bind)

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()
    }

    private fun initViews() = with(viewBinding) {

//        this.buttonUpdateApiKey.setOnClickListener {
//            val apiKey = this.editTextApiKey.text.toString().trim()
//            profileViewModel.reduce(event = ProfileEvent.OnUpdateApiKeyClicked(apiKey))
//            hideKeyboard()
//        }

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

//        profileViewModel.updateProfileState.observe(viewLifecycleOwner) { state ->
//            when (state) {
//                is UpdateProfileState.Initial -> {
//                    showUpdateButtonLoading(false)
//                    hideFieldError()
//                }
//                is UpdateProfileState.Loading -> {
//                    hideFieldError()
//                    showUpdateButtonLoading(true)
//                }
//                is UpdateProfileState.Success -> {
//                    showUpdateButtonLoading(false)
//                    hideFieldError()
//                }
//
//                is UpdateProfileState.Error.FieldError -> {
//                    showUpdateButtonLoading(false)
//                    showFieldError(state.message)
//                }
//                is UpdateProfileState.Error.GlobalError -> {
//                    showUpdateButtonLoading(false)
//
//                    ErrorDialogUtil.showErrorDialog(
//                        context = requireContext(),
//                        message = state.message
//                    )
//                }
//            }
//        }

        profileViewModel.clearApiKeyState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ClearJwtState.Initial -> {
                    showLogOutButtonLoading(false)
                }
                is ClearJwtState.Loading -> {
                    showLogOutButtonLoading(true)
                }
                is ClearJwtState.Success -> {
                    showLogOutButtonLoading(false)
                }

                is ClearJwtState.Error -> {
                    showLogOutButtonLoading(false)

                    ErrorDialogUtil.showErrorDialog(
                        context = requireContext(),
                        message = state.message
                    )
                }
            }
        }
    }

//    private fun showUpdateButtonLoading(isLoading: Boolean) {
//        viewBinding.buttonUpdateApiKey.isEnabled = !isLoading
//    }

    private fun showLogOutButtonLoading(isLoading: Boolean) {
        viewBinding.buttonLogOut.isEnabled = !isLoading
    }

//    private fun showFieldError(message: String) {
//        viewBinding.textError.apply {
//            text = message
//            visibility = View.VISIBLE
//        }
//    }

//    private fun hideFieldError() {
//        viewBinding.textError.visibility = View.GONE
//    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
