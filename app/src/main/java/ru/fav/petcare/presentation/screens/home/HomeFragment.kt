package ru.fav.petcare.presentation.screens.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.fav.petcare.R
import ru.fav.petcare.databinding.FragmentHomeBinding
import kotlin.getValue

@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.fragment_home) {

    private var viewBinding: FragmentHomeBinding? = null

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentHomeBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding = null
    }
}