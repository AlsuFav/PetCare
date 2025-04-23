package ru.fav.petcare.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.fav.petcare.R
import ru.fav.petcare.databinding.ActivityMainBinding


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var viewBinding: ActivityMainBinding? = null
    private var navController: NavController? = null
    private var appBarConfiguration: AppBarConfiguration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding?.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.menu_home_tab, R.id.menu_appointments_tab, R.id.menu_pets_tab, R.id.menu_account_tab)
        )

        setupBottomNavigation()
        setupNavigationListeners()
    }

    private fun setupBottomNavigation() {
        navController?.let {
            viewBinding?.mainBottomNavigation?.setupWithNavController(it)
        }

        navController?.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.authorizationFragment, R.id.registrationFragment -> hideBottomNavigation()
                else -> showBottomNavigation()
            }
        }
    }

    private fun setupNavigationListeners() {
        viewBinding?.mainBottomNavigation?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home_tab -> {
                    navController?.navigate(R.id.homeFragment)
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

    fun showBottomNavigation() {
        viewBinding?.mainBottomNavigation?.visibility = View.VISIBLE
    }

    fun hideBottomNavigation() {
        viewBinding?.mainBottomNavigation?.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController?.navigateUp() == true || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        viewBinding = null
        super.onDestroy()
    }
}