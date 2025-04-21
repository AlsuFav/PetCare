package ru.fav.petcare.presentation.screens.registration

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.fav.petcare.R
import ru.fav.petcare.databinding.FragmentRegistrationBinding
import ru.fav.petcare.presentation.MainActivity
import ru.fav.petcare.presentation.screens.authorization.AuthorizationFragment
import ru.fav.petcare.presentation.screens.home.HomeFragment
import ru.fav.petcare.presentation.utils.validators.PhoneValidator
import ru.fav.petcare.presentation.utils.watchers.PhoneNumberTextWatcher
import ru.fav.petcare.presentation.base.NavigationAction
import ru.fav.petcare.presentation.screens.authorization.AuthorizationViewModel
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
            register(firstName, lastName, phone, password, confirmPassword)
        }

        viewBinding?.buttonSignIn?.setOnClickListener {
            (requireActivity() as? MainActivity)?.navigate(
                destination = AuthorizationFragment(),
                destinationTag = AuthorizationFragment.Companion.AUTHORIZATION_TAG,
                action = NavigationAction.REPLACE,
                isAddToBackStack = false)
        }
    }

    private fun observeViewModel() = with(registrationViewModel) {
        jwtFlow.observe(viewLifecycleOwner) { jwtModel ->
            jwtModel?.let {
                (requireActivity() as? MainActivity)?.apply {
                    showBottomNavigation()
                    navigate(
                        destination = HomeFragment(),
                        destinationTag = HomeFragment.HOME_TAG,
                        action = NavigationAction.REPLACE,
                        isAddToBackStack = true
                    )
                }
            }
        }

//        loadingFlow.observe(viewLifecycleOwner) { isLoading ->
//            viewBinding?.progressBar?.isVisible = isLoading
//        }

        registrationViewModel.errorFlow.observe(viewLifecycleOwner) { message ->
            showError(message)
        }
    }

    private fun register(
        firstName: String,
        lastName: String,
        phone: String,
        password: String,
        confirmPassword: String
    ) {
        val errorText = when {
            lastName.isEmpty() || firstName.isEmpty() || phone.isEmpty() || password.isEmpty() -> getString(R.string.fill_all_fields)
            !PhoneValidator.isValid(phone) -> getString(R.string.invalid_phone_format)
            password != confirmPassword -> getString(R.string.passwords_are_not_the_same)

            else -> null
        }

        if (errorText != null) {
            showError(errorText)
        } else {
            hideError()
            registrationViewModel.getJwt(firstName, lastName, phone, password, confirmPassword)
        }
    }

    private fun showError(message: String) {
        viewBinding?.textError?.let {
            it.text = message
            it.visibility = View.VISIBLE
        }
    }

    private fun hideError() {
        viewBinding?.textError?.visibility = View.GONE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    companion object {
        const val REGISTRATION_TAG = "REGISTRATION_TAG"
    }
}
