package ru.fav.petcare.presentation.screens.registration

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.fav.petcare.R
import ru.fav.petcare.databinding.FragmentRegistrationBinding
import ru.fav.petcare.presentation.utils.ErrorDialogUtil
import ru.fav.petcare.presentation.utils.watchers.PhoneNumberTextWatcher
import ru.fav.petcare.utils.observe
import kotlin.getValue

@AndroidEntryPoint
class RegistrationFragment: Fragment(R.layout.fragment_registration) {

    private var viewBinding: FragmentRegistrationBinding? = null

    private val registrationViewModel: RegistrationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentRegistrationBinding.bind(view)
        initViews()
        observeViewModel()
    }

    private fun initViews() {
        viewBinding?.editTextPhone?.addTextChangedListener(PhoneNumberTextWatcher(viewBinding!!.editTextPhone))

        viewBinding?.buttonSignUp?.setOnClickListener {
            val firstName = viewBinding?.editTextFirstName?.text.toString().trim()
            val lastName = viewBinding?.editTextLastName?.text.toString().trim()
            val phone = viewBinding?.editTextPhone?.text.toString().trim()
            val password = viewBinding?.editTextPassword?.text.toString().trim()
            val confirmPassword = viewBinding?.editTextConfirmPassword?.text.toString().trim()
            registrationViewModel.register(
                firstName = firstName,
                lastName = lastName,
                phone = phone,
                password = password,
                confirmPassword = confirmPassword
            )
        }

        viewBinding?.buttonSignIn?.setOnClickListener {
            navigateToAuthorizationFragment()
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
                    navigateToHomeFragment()
                }

                is RegistrationState.Error.FieldError -> {
                    showLoading(false)
                    showError(state.message)
                }
                is RegistrationState.Error.GlobalError -> {
                    showLoading(false)

                    ErrorDialogUtil.showErrorDialog(
                        context = requireContext(),
                        message = state.message
                    )
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        viewBinding?.buttonSignIn?.isEnabled = !isLoading
    }

    private fun showError(message: String) {
        viewBinding?.textError?.apply {
            text = message
            visibility = View.VISIBLE
        }
    }

    private fun hideError() {
        viewBinding?.textError?.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    private fun navigateToAuthorizationFragment() {
        findNavController().navigate(R.id.action_registration_to_authorization)
    }

    private fun navigateToHomeFragment() {
        findNavController().navigate(R.id.action_registration_to_home)
    }
}
