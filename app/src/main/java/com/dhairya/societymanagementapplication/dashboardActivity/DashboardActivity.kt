package com.dhairya.societymanagementapplication.dashboardActivity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.dhairya.societymanagementapplication.R

class DashboardActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_dashboard) as NavHostFragment
        navController = navHostFragment.findNavController()



        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.dashBoardFragment) {
                actionBar?.hide()
            } else {
                actionBar?.show()
            }
        }

    }
}
const val AUTH_RESULT_OK = Activity.RESULT_FIRST_USER