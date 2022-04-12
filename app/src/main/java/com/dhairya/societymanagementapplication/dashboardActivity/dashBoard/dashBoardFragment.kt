package com.dhairya.societymanagementapplication.dashboardActivity.dashBoard

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.data.profileData
import com.dhairya.societymanagementapplication.data.residentsData
import com.dhairya.societymanagementapplication.databinding.FragmentDashBoardBinding
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
    private val profile_data = FirebaseFirestore.getInstance().collection("profileData")

    //    private lateinit var userRole: String
    private var userName : MutableList<profileData> = mutableListOf()
    private var resi_Data: MutableList<residentsData> = mutableListOf()
//    lateinit var tempName:String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {
            resi_Data =  resident.whereEqualTo("uid", Firebase.auth.currentUser!!.uid).get()
                .await().toObjects(residentsData::class.java)

            userName = profile_data.whereEqualTo("memberid",Firebase.auth.currentUser!!.uid).get()
                .await().toObjects(profileData::class.java)

            setName(userName[0].fullName)
//            tempName = userName[0].fullName.toString()


          //  Toast.makeText(context, userName[0].fullName.toString(), Toast.LENGTH_SHORT).show()

            ckeckUserrole(resi_Data[0].role)


        }



        binding = FragmentDashBoardBinding.bind(view)
        binding.apply {

           // dashboardName.setText(tempName)

            btnExpenseSheet.setOnClickListener {
                findNavController().navigate(dashBoardFragmentDirections.actionDashBoardFragmentToExpenseSheetFragment())
            }

            btnResidents.setOnClickListener {
                findNavController().navigate(dashBoardFragmentDirections.actionDashBoardFragmentToResidentListFragment())
            }
            btnPayMaintenance.setOnClickListener {
                findNavController().navigate(dashBoardFragmentDirections.actionDashBoardFragmentToMaintenanceInvoiceFragment())
            }

            btnAddMember.setOnClickListener {
                findNavController().navigate(dashBoardFragmentDirections.actionDashBoardFragmentToAddMemberFragment())
            }


        }


    }

    private fun setName(fullName: String) {

        binding.dashboardName.setText(fullName)

    }

    private fun ckeckUserrole(role: String) {

        if(role == "member" || role == "Treasurer")
        {
            binding.cvAddMembers.isVisible = false

        }


    }

}