package ru.fav.petcare.presentation.screens.authorization

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.fav.petcare.R
import ru.fav.petcare.databinding.FragmentAuthorizationBinding
import ru.fav.petcare.presentation.MainActivity
import ru.fav.petcare.presentation.screens.home.HomeFragment
import ru.fav.petcare.presentation.screens.registration.RegistrationFragment
import ru.fav.petcare.presentation.utils.validators.PhoneValidator
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
            login(phone, password)
        }

        this?.buttonSignUp?.setOnClickListener {
            (requireActivity() as? MainActivity)?.navigate(
                destination = RegistrationFragment(),
                destinationTag = RegistrationFragment.REGISTRATION_TAG,
                action = NavigationAction.REPLACE,
                isAddToBackStack = false
            )
        }
    }

    private fun observeViewModel() = with(authorizationViewModel) {
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

        authorizationViewModel.errorFlow.observe(viewLifecycleOwner) { message ->
            showError(message)
        }
    }

    private fun login(phone: String, password: String) {
        val errorText = when {
            phone.isEmpty() || password.isEmpty() -> getString(R.string.fill_all_fields)
            !PhoneValidator.isValid(phone) -> getString(R.string.invalid_phone_format)
            else -> null
        }

        if (errorText != null) {
            showError(errorText)
        } else {
            hideError()
            authorizationViewModel.getJwt(phone, password)
        }
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

    companion object {
        const val AUTHORIZATION_TAG = "AUTHORIZATION_TAG"
    }
}

