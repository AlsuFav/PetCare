package ru.fav.petcare.home.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import ru.fav.petcare.home.databinding.FragmentHomeBinding
import ru.fav.petcare.home.R
import kotlin.getValue

@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.fragment_home) {

    private val viewBinding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}