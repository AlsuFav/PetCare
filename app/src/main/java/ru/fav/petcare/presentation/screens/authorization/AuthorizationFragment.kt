package ru.fav.petcare.presentation.screens.authorization

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.fav.petcare.R
import ru.fav.petcare.databinding.FragmentAuthorizationBinding
import ru.fav.petcare.presentation.MainActivity
import ru.fav.petcare.presentation.screens.home.HomeFragment
import ru.fav.petcare.presentation.screens.registration.RegistrationFragment
import ru.fav.petcare.presentation.utils.validators.PhoneValidator
import ru.fav.petcare.presentation.utils.watchers.PhoneNumberTextWatcher
import ru.fav.petcare.presentation.base.NavigationAction

@AndroidEntryPoint
class AuthorizationFragment: Fragment(R.layout.fragment_authorization) {

    private var viewBinding: FragmentAuthorizationBinding? = null

    private val authorizationViewModel: AuthorizationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentAuthorizationBinding.bind(view)
        initViews()
    }

    private fun initViews() {
        viewBinding?.editTextPhone?.addTextChangedListener(PhoneNumberTextWatcher(viewBinding!!.editTextPhone))

        viewBinding?.buttonSignIn?.setOnClickListener {
            login()
        }

        viewBinding?.buttonSignUp?.setOnClickListener {
            (requireActivity() as? MainActivity)?.navigate(
                destination = RegistrationFragment(),
                destinationTag = RegistrationFragment.Companion.REGISTRATION_TAG,
                action = NavigationAction.REPLACE,
                isAddToBackStack = false)
        }
    }

    private fun login() {
        val phone = viewBinding?.editTextPhone?.text.toString().trim()
        val password = viewBinding?.editTextPassword?.text.toString().trim()

        val errorText = when {
            phone.isEmpty() || password.isEmpty() -> getString(R.string.fill_all_fields)
            !PhoneValidator.isValid(phone) -> getString(R.string.invalid_phone_format)
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


    override fun onDestroy() {
        super.onDestroy()
        viewBinding = null
    }

    companion object {
        const val AUTHORIZATION_TAG = "AUTHORIZATION_TAG"
    }
}
