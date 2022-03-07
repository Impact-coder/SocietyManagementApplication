package com.dhairya.societymanagementapplication.dashboardActivity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.dhairya.societymanagementapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        var nav1=findViewById<BottomNavigationView>(R.id.bottomnavbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_dashboard) as NavHostFragment
        navController = navHostFragment.findNavController()


    }
}
const val AUTH_RESULT_OK = Activity.RESULT_FIRST_USER