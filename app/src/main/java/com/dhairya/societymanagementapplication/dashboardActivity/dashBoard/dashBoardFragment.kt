package com.dhairya.societymanagementapplication.dashboardActivity.dashBoard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.authActivity.authfragments.ui.login.loginFragmentDirections
import com.dhairya.societymanagementapplication.authActivity.authfragments.ui.login.loginViewModel
import com.dhairya.societymanagementapplication.data.residentsData
import com.dhairya.societymanagementapplication.databinding.FragmentDashBoardBinding
import com.dhairya.societymanagementapplication.databinding.FragmentLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class dashBoardFragment : Fragment(R.layout.fragment_dash_board) {

    private val viewModel: dashBoardViewModel by viewModels()
    private lateinit var binding: FragmentDashBoardBinding
    private val resident = FirebaseFirestore.getInstance().collection("residents")
    //    private lateinit var userRole: String
    private var resi_Data: MutableList<residentsData> = mutableListOf()
//    var addUser = findViewById<>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {
            resi_Data =  resident.whereEqualTo("uid", Firebase.auth.currentUser!!.uid).get()
                .await().toObjects(residentsData::class.java)

            ckeckUserrole(resi_Data[0].role)
        }



        binding = FragmentDashBoardBinding.bind(view)
        binding.apply {

            btnExpenseSheet.setOnClickListener {
                findNavController().navigate(dashBoardFragmentDirections.actionDashBoardFragmentToExpenseSheetFragment())
            }

            btnResidents.setOnClickListener {
                findNavController().navigate(dashBoardFragmentDirections.actionDashBoardFragmentToResidentListFragment())
            }
            btnPayMaintenance.setOnClickListener {
                findNavController().navigate(dashBoardFragmentDirections.actionDashBoardFragmentToMaintenanceInvoiceFragment())
            }




        }


    }

    private fun ckeckUserrole(role: String) {

        if(role == "Secretary")
        {

        }

    }

}