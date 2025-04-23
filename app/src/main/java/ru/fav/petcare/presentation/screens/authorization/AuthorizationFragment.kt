package ru.fav.petcare.presentation.screens.authorization

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.fav.petcare.R
import ru.fav.petcare.databinding.FragmentAuthorizationBinding
import ru.fav.petcare.presentation.MainActivity
import ru.fav.petcare.presentation.screens.home.HomeFragment
import ru.fav.petcare.presentation.screens.registration.RegistrationFragment
import ru.fav.petcare.presentation.utils.watchers.PhoneNumberTextWatcher
import ru.fav.petcare.presentation.base.NavigationAction
import ru.fav.petcare.utils.observe

@AndroidEntryPoint
class AuthorizationFragment : Fragment(R.layout.fragment_authorization) {

    private var viewBinding: FragmentAuthorizationBinding? = null

    private val authorizationViewModel: AuthorizationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentAuthorizationBinding.bind(view)
        initViews()
        observeViewModel()
    }

    private fun initViews() = with(viewBinding) {
        this?.editTextPhone?.addTextChangedListener(PhoneNumberTextWatcher(this.editTextPhone))

        this?.buttonSignIn?.setOnClickListener {
            val phone = this.editTextPhone.text.toString().trim()
            val password = this.editTextPassword.text.toString().trim()
            authorizationViewModel.authorize(phone, password)
        }

        this?.buttonSignUp?.setOnClickListener {
            navigateToRegistrationFragment()
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
                    navigateToHomeFragment()
                }

                is AuthorizationState.Error.FieldError -> {
                    showLoading(false)
                    showError(state.message)
                }
                is AuthorizationState.Error.GlobalError -> {
                    showLoading(false)
                    showToast(state.message)
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

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun hideError() {
        viewBinding?.textError?.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    private fun navigateToRegistrationFragment() {
        (requireActivity() as? MainActivity)?.apply {
            hideBottomNavigation()
            navigate(
                destination = RegistrationFragment(),
                destinationTag = RegistrationFragment.REGISTRATION_TAG,
                action = NavigationAction.REPLACE,
                isAddToBackStack = false
            )
        }
    }

    private fun navigateToHomeFragment() {
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

    companion object {
        const val AUTHORIZATION_TAG = "AUTHORIZATION_TAG"
    }
}

