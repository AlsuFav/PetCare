package ru.fav.petcare.splash.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import ru.fav.petcare.presentation.util.ErrorDialogUtil
import ru.fav.petcare.splash.R
import ru.fav.petcare.splash.databinding.FragmentSplashBinding
import ru.fav.petcare.splash.ui.state.SplashEvent
import ru.fav.petcare.splash.ui.state.SplashState
import ru.fav.petcare.util.extension.observe

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val viewBinding: FragmentSplashBinding by viewBinding(FragmentSplashBinding::bind)

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        splashViewModel.reduce(event = SplashEvent.CheckApiKey)
    }

    private fun observeViewModel() {
        splashViewModel.splashState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SplashState.Loading -> {}
                is SplashState.Success -> {}
                is SplashState.Error.NoApiKey -> {}

                is SplashState.Error.GlobalError -> {
                    ErrorDialogUtil.showErrorDialog(
                        context = requireContext(),
                        message = state.message
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
