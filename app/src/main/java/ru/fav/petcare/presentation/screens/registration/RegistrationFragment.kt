package ru.fav.petcare.presentation.screens.registration

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.fav.petcare.R
import ru.fav.petcare.databinding.FragmentRegistrationBinding
import ru.fav.petcare.presentation.MainActivity
import ru.fav.petcare.presentation.screens.authorization.AuthorizationFragment
import ru.fav.petcare.presentation.screens.home.HomeFragment
import ru.fav.petcare.presentation.utils.validators.PhoneValidator
import ru.fav.petcare.presentation.utils.watchers.PhoneNumberTextWatcher
import ru.fav.petcare.presentation.base.NavigationAction

class RegistrationFragment: Fragment(R.layout.fragment_registration) {

    private var viewBinding: FragmentRegistrationBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentRegistrationBinding.bind(view)

        initViews()
    }

    private fun initViews() {
        viewBinding?.editTextPhone?.addTextChangedListener(PhoneNumberTextWatcher(viewBinding!!.editTextPhone))

        viewBinding?.buttonSignUp?.setOnClickListener {
            registerUser()
        }

        viewBinding?.buttonSignIn?.setOnClickListener {
            (requireActivity() as? MainActivity)?.navigate(
                destination = AuthorizationFragment(),
                destinationTag = AuthorizationFragment.Companion.AUTHORIZATION_TAG,
                action = NavigationAction.REPLACE,
                isAddToBackStack = false)
        }
    }

        override fun onDestroy() {
        super.onDestroy()
        viewBinding = null
    }


    private fun registerUser() {
        val surname = viewBinding?.editTextSurname?.text.toString().trim()
        val name = viewBinding?.editTextName?.text.toString().trim()
        val phone = viewBinding?.editTextPhone?.text.toString().trim()
        val password = viewBinding?.editTextPassword?.text.toString().trim()
        val confirmPassword = viewBinding?.editTextConfirmPassword?.text.toString().trim()

        val errorText = when {
            surname.isEmpty() || name.isEmpty() || phone.isEmpty() || password.isEmpty() -> getString(R.string.fill_all_fields)
            !PhoneValidator.isValid(phone) -> getString(R.string.invalid_phone_format)
            password != confirmPassword -> getString(R.string.passwords_are_not_the_same)

            else -> null
        }

        if (errorText != null) {
            showError(errorText)
            return
        }

        hideError()

        (requireActivity() as? MainActivity)?.showBottomNavigation()
        (requireActivity() as? MainActivity)?.navigate(
            destination = HomeFragment(),
            destinationTag = HomeFragment.Companion.HOME_TAG,
            action = NavigationAction.REPLACE,
            isAddToBackStack = true
        )

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

    companion object {
        const val REGISTRATION_TAG = "REGISTRATION_TAG"
    }
}
