package com.dhairya.societymanagementapplication.authActivity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.dhairya.societymanagementapplication.R

class AuthActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        supportActionBar?.hide()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_auth) as NavHostFragment
        navController = navHostFragment.findNavController()



        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.loginFragment) {
                actionBar?.hide()
            } else {
                actionBar?.show()
            }
        }

    }
}

const val AUTH_RESULT_OK = Activity.RESULT_FIRST_USER