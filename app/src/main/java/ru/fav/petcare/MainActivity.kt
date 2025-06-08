package ru.fav.petcare

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import ru.fav.petcare.app.R
import ru.fav.petcare.app.databinding.ActivityMainBinding
import ru.fav.petcare.navigation.Nav
import ru.fav.petcare.navigation.NavMain
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Nav.Provider {

    @Inject
    lateinit var nav: Nav

    private val viewBinding by viewBinding(ActivityMainBinding::bind)
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupSystemWindowInsets()
        setupNavigation()
        setupBottomNavigation()
    }

    private fun setupSystemWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                view.paddingLeft,
                systemBars.top,
                view.paddingRight,
                0
            )
            insets
        }
    }

    private fun setupNavigation() {
        if (navController == null) {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController = navHostFragment.navController
        }
        nav.setNavProvider(this)

        navController?.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                ru.fav.petcare.authorization.R.id.destination_authorization -> showBottomNavigation(false)
                ru.fav.petcare.registration.R.id.destination_registration -> showBottomNavigation(false)
                else -> showBottomNavigation(true)
            }
        }
    }

    private fun setupBottomNavigation() {
        navController?.let { controller ->
            viewBinding.mainBottomNavigation.setupWithNavController(controller)
        }

        viewBinding.mainBottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    (nav as? NavMain)?.goToHomePage()
                    true
                }
                R.id.nav_all_appointments -> {
                    (nav as? NavMain)?.goToAllAppointmentsPage()
                    true
                }
                R.id.nav_all_pets -> {
                    (nav as? NavMain)?.goToAllPetsPage()
                    true
                }
                R.id.nav_profile -> {
                    (nav as? NavMain)?.goToProfilePage()
                    true
                }
                else -> false
            }
        }
    }

    private fun showBottomNavigation(show: Boolean) {
        viewBinding.mainBottomNavigation.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun getNavController(): NavController? {
        return navController
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::nav.isInitialized) {
            nav.clearNavProvider(navProvider = this)
        }
    }
}
