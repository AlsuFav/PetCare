package ru.fav.petcare.presentation.screens.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.fav.petcare.R
import ru.fav.petcare.databinding.FragmentHomeBinding
import ru.fav.petcare.presentation.screens.authorization.AuthorizationViewModel
import kotlin.getValue

@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.fragment_home) {

    private var viewBinding: FragmentHomeBinding? = null

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentHomeBinding.bind(view)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding = null
    }

    companion object {
        const val HOME_TAG = "HOME_TAG"
    }
}