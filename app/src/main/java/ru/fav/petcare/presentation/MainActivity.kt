package ru.fav.petcare.presentation

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import ru.fav.petcare.R
import ru.fav.petcare.databinding.ActivityMainBinding
import ru.fav.petcare.presentation.base.BaseActivity
import ru.fav.petcare.presentation.base.NavigationAction
import ru.fav.petcare.presentation.screens.authorization.AuthorizationFragment

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override val mainContainerId = R.id.main_fragment_container
    private var viewBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding?.root)

        viewBinding?.mainBottomNavigation?.setOnItemReselectedListener {}

        hideBottomNavigation()

        navigateToAuthorizationFragment()

    }


    override fun onDestroy() {
        viewBinding = null
        super.onDestroy()
    }


    fun showBottomNavigation() {
        viewBinding?.mainBottomNavigation?.visibility = View.VISIBLE
    }


    fun hideBottomNavigation() {
        viewBinding?.mainBottomNavigation?.visibility = View.GONE
    }


    private fun navigateToAuthorizationFragment() {
        hideBottomNavigation()
        navigate(
            destination = AuthorizationFragment(),
            destinationTag = AuthorizationFragment.AUTHORIZATION_TAG,
            action = NavigationAction.REPLACE,
            isAddToBackStack = false
        )
    }


    private fun setupBottomNavigation() {
        val bottomNavigationView = viewBinding?.mainBottomNavigation
        bottomNavigationView?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home_tab -> {
                    true
                }
                R.id.menu_appointments_tab -> {
                    true
                }
                R.id.menu_pets_tab -> {
                    true
                }
                R.id.menu_account_tab -> {
                    true
                }
                else -> false
            }
        }
    }
}
