package com.dhairya.societymanagementapplication.dashboardActivity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.data.residentsData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DashboardActivity : AppCompatActivity() {

    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        supportActionBar?.hide()


        var nav1 = findViewById<BottomNavigationView>(R.id.bottomnavbar)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_graph_dashboard) as NavHostFragment
        navController = navHostFragment.findNavController()
        nav1.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.dashBoardFragment || destination.id == R.id.notices
                || destination.id == R.id.communicationFragment || destination.id == R.id.profileFragment
            ) {
                actionBar?.hide()
                bottomnavbar.visibility = View.VISIBLE
            } else {
                actionBar?.show()
                bottomnavbar.visibility = View.GONE
            }
        }
    }


}

const val AUTH_RESULT_OK = Activity.RESULT_FIRST_USER