package com.dhairya.societymanagementapplication.dashboardActivity.dashBoard

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.authActivity.AuthActivity
import com.dhairya.societymanagementapplication.dashboardActivity.DashboardActivity
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
                findNavController().navigate(dashBoardFragmentDirections.actionDashBoardFragmentToExpenseSheetFragment(),null)
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

            btnAddExpense.setOnClickListener {
                findNavController().navigate(dashBoardFragmentDirections.actionDashBoardFragmentToAddTransactionFragment())

            }

            popupMenu.setOnClickListener {
                val popup=PopupMenu(context,it)
                popup.setOnMenuItemClickListener { item ->
                    when(item.itemId){
                        R.id.btn_about ->{
                            Toast.makeText(context, "About", Toast.LENGTH_SHORT).show()
                        }
                        R.id.btn_logout -> {
                            val builder=AlertDialog.Builder(context)
                            builder.setTitle("Logout")
                            builder.setIcon(R.drawable.ic_logout)
                            builder.setMessage("Are you Sure you want to logout")
                                .setPositiveButton("Yes"){dialogInterface,which ->
                                    Intent(requireContext(), AuthActivity::class.java).also {
                                        startActivity(it)
                                        requireActivity().finish()
                                    }
                                }
                                .setNeutralButton("Cancel"){dialogInterface,which ->

                                }
                            val alertDialog:AlertDialog=builder.create()
                            alertDialog.setCancelable(false)
                            alertDialog.show()
                        }
                    }
                    true
                }
                popup.inflate(R.menu.popup_menu)
                try {
                    val fieldMPopup=PopupMenu::class.java.getDeclaredField("mPopup")
                    fieldMPopup.isAccessible=true
                    val mPoopup=fieldMPopup.get(popup)
                    mPoopup.javaClass
                        .getDeclaredMethod("setForceShowIcon",Boolean::class.java)
                        .invoke(mPoopup,true)
                }catch (e:Exception){
                    Log.e("main","Error Menu Icon")
                }
                popup.show()
            }
        }


    }

    private fun setName(fullName: String) {

        binding.dashboardName.setText(fullName)

    }

    private fun ckeckUserrole(role: String) {

        if(role == "member" || role == "treasurer")
        {
            binding.cvAddMembers.isVisible = false

        }


    }

}