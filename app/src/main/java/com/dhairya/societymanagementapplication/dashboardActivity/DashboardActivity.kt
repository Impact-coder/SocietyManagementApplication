package com.dhairya.societymanagementapplication.dashboardActivity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.dhairya.societymanagementapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

//        var nav1=findViewById<BottomNavigationView>(R.id.bottomnavbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_graph_dashboard) as NavHostFragment
        navController = navHostFragment.findNavController()
//        nav1.setupWithNavController(navController)
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