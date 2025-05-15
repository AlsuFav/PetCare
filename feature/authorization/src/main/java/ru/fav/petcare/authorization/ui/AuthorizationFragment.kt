package ru.fav.petcare.authorization.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import ru.fav.petcare.authorization.databinding.FragmentAuthorizationBinding
import ru.fav.petcare.authorization.R
import ru.fav.petcare.authorization.ui.state.AuthorizationEvent
import ru.fav.petcare.authorization.ui.state.AuthorizationState
import ru.fav.petcare.presentation.util.watchers.PhoneNumberTextWatcher
import ru.fav.petcare.util.extension.observe
import ru.fav.petcare.util.extension.showErrorDialog

@AndroidEntryPoint
class AuthorizationFragment : Fragment(R.layout.fragment_authorization) {

    private val viewBinding: FragmentAuthorizationBinding by viewBinding(
        FragmentAuthorizationBinding::bind)

    private val authorizationViewModel: AuthorizationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()
    }

    private fun initViews() = with(viewBinding) {
        this.editTextPhone.addTextChangedListener(PhoneNumberTextWatcher(this.editTextPhone))

        this.buttonSignIn.setOnClickListener {
            val phone = this.editTextPhone.text.toString().trim()
            val password = this.editTextPassword.text.toString().trim()
            authorizationViewModel.reduce(event = AuthorizationEvent.OnSignInClicked(phone, password))
        }

        this.buttonSignUp.setOnClickListener {
            authorizationViewModel.reduce(event = AuthorizationEvent.OnSignUpClicked)
        }
    }

    private fun observeViewModel() {
        authorizationViewModel.authorizationState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthorizationState.Initial -> {
                    showLoading(false)
                    hideError()
                }
                is AuthorizationState.Loading -> {
                    hideError()
                    showLoading(true)
                }
                is AuthorizationState.Success -> {
                    showLoading(false)
                    hideError()
                }

                is AuthorizationState.Error.FieldError -> {
                    showLoading(false)
                    showError(state.message)
                }
                is AuthorizationState.Error.GlobalError -> {
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

