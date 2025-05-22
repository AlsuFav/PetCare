package ru.fav.petcare.registration.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import ru.fav.petcare.presentation.util.watchers.PhoneNumberTextWatcher
import ru.fav.petcare.registration.R
import ru.fav.petcare.registration.databinding.FragmentRegistrationBinding
import ru.fav.petcare.registration.ui.state.RegistrationEvent
import ru.fav.petcare.registration.ui.state.RegistrationState
import ru.fav.petcare.util.extension.observe
import ru.fav.petcare.util.extension.showErrorDialog
import kotlin.getValue

@AndroidEntryPoint
class RegistrationFragment: Fragment(R.layout.fragment_registration) {

    private val viewBinding: FragmentRegistrationBinding by viewBinding(FragmentRegistrationBinding::bind)

    private val registrationViewModel: RegistrationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()
    }

    private fun initViews() {
        viewBinding.editTextPhone.addTextChangedListener(PhoneNumberTextWatcher(viewBinding.editTextPhone))

        viewBinding.buttonSignUp.setOnClickListener {
            val firstName = viewBinding.editTextFirstName.text.toString().trim()
            val lastName = viewBinding.editTextLastName.text.toString().trim()
            val phone = viewBinding.editTextPhone.text.toString().trim()
            val password = viewBinding.editTextPassword.text.toString().trim()
            val confirmPassword = viewBinding.editTextConfirmPassword.text.toString().trim()
            registrationViewModel.reduce(event = RegistrationEvent.OnSignUpClicked(
                firstName = firstName,
                lastName = lastName,
                phone = phone,
                password = password,
                confirmPassword = confirmPassword
            ))
        }

        viewBinding.buttonSignIn.setOnClickListener {
            registrationViewModel.reduce(event = RegistrationEvent.OnSignInClicked)
        }
    }

    private fun observeViewModel() {
        registrationViewModel.registrationState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is RegistrationState.Initial -> {
                    showLoading(false)
                    hideError()
                }
                is RegistrationState.Loading -> {
                    hideError()
                    showLoading(true)
                }
                is RegistrationState.Success -> {
                    showLoading(false)
                    hideError()
                }

                is RegistrationState.Error.FieldError -> {
                    showLoading(false)
                    showError(state.message)
                }
                is RegistrationState.Error.GlobalError -> {
                    showLoading(false)

                    showErrorDialog(
                        message = state.message
                    )
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        viewBinding.buttonSignIn.isEnabled = !isLoading
    }

    private fun showError(message: String) {
        viewBinding.textError.apply {
            text = message
            visibility = View.VISIBLE
        }
    }

    private fun hideError() {
        viewBinding.textError.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
