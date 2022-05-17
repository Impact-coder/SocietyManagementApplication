package com.dhairya.societymanagementapplication.dashboardActivity.dashBoard

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.authActivity.AuthActivity
import com.dhairya.societymanagementapplication.dashboardActivity.adapter.dashboardNoticeAdapter
import com.dhairya.societymanagementapplication.data.noticeData
import com.dhairya.societymanagementapplication.data.profileData
import com.dhairya.societymanagementapplication.data.residentsData
import com.dhairya.societymanagementapplication.databinding.FragmentDashBoardBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class dashBoardFragment : Fragment(R.layout.fragment_dash_board) {

    private val viewModel: dashBoardViewModel by viewModels()
    private lateinit var binding: FragmentDashBoardBinding
    private val resident_data = FirebaseFirestore.getInstance().collection("residents")
    private val profile_data = FirebaseFirestore.getInstance().collection("profileData")

//    private lateinit var dashboardnoticeArrayList: ArrayList<noticeData>
//    private lateinit var dashboardnoticesDisplayAdapter: dashboardNoticeAdapter
//    private lateinit var recycleView: RecyclerView

    private val notice_data = FirebaseFirestore.getInstance().collection("noticeData")

    //    private lateinit var userRole: String

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDashBoardBinding.bind(view)


        CoroutineScope(Dispatchers.Main).launch {

            val userName =
                profile_data.document(Firebase.auth.currentUser!!.uid).get().await()
                    .toObject(profileData::class.java)!!
            binding.dashboardName.text = userName.fullName


            val resident = resident_data.document(Firebase.auth.currentUser!!.uid).get().await()
                .toObject(residentsData::class.java)!!
//            binding.dashboardName.text = resident.email
            if (resident.role == "member") {
                binding.cvAddMembers.isVisible = false
                binding.cvAddExpense.isVisible = false

            }

            if (resident.role == "treasurer") {
                binding.cvAddMembers.isVisible = false
            }
        }

        binding.apply {

//                recycleView = binding.dashboardRecycleView
//                dashboardnoticeArrayList = arrayListOf()
//                recycleView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
//
            CoroutineScope(Dispatchers.Main).launch {

                val list =
                    notice_data.orderBy("dateTime", Query.Direction.DESCENDING).limit(1).get()
                        .await().toObjects(noticeData::class.java)!!

                dashboardNoticeDate.text = list[0].dateTime.toString()
                dashboardNoticeSubject.text = list[0].title.toString()

            }

            btnExpenseSheet.setOnClickListener {
                findNavController().navigate(
                    dashBoardFragmentDirections.actionDashBoardFragmentToExpenseSheetFragment(),
                    null
                )
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
            btnNotices.setOnClickListener {
                findNavController().navigate(dashBoardFragmentDirections.actionDashBoardFragmentToNoticeFragment())
            }
            btnFileComplain.setOnClickListener {
                findNavController().navigate(dashBoardFragmentDirections.actionDashBoardFragmentToFileComplain())
            }

            btnShowComplains.setOnClickListener {
                findNavController().navigate(dashBoardFragmentDirections.actionDashBoardFragmentToComplainsListFragment())
            }

            btnMyComplain.setOnClickListener {
                findNavController().navigate(dashBoardFragmentDirections.actionDashBoardFragmentToMyComplainsFragment())
            }


            popupMenu.setOnClickListener {
                val popup = PopupMenu(context, it)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.btn_about -> {
                            Toast.makeText(context, "About", Toast.LENGTH_SHORT).show()
                        }
                        R.id.btn_logout -> {
                            val builder = AlertDialog.Builder(context)
                            builder.setTitle("Logout")
                            builder.setIcon(R.drawable.ic_logout)
                            builder.setMessage("Are you Sure you want to logout")
                                .setPositiveButton("Yes") { dialogInterface, which ->
                                    Intent(requireContext(), AuthActivity::class.java).also {
                                        startActivity(it)
                                        requireActivity().finish()
                                    }
                                }
                                .setNeutralButton("Cancel") { dialogInterface, which ->

                                }
                            val alertDialog: AlertDialog = builder.create()
                            alertDialog.setCancelable(false)
                            alertDialog.show()
                        }
                    }
                    true
                }
                popup.inflate(R.menu.popup_menu)
                try {
                    val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
                    fieldMPopup.isAccessible = true
                    val mPoopup = fieldMPopup.get(popup)
                    mPoopup.javaClass
                        .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                        .invoke(mPoopup, true)
                } catch (e: Exception) {
                    Log.e("main", "Error Menu Icon")
                }
                popup.show()
            }
        }


    }


    private fun showProgress(bool: Boolean) {
        binding.apply {
            animationView.isVisible = bool
            if (bool) {
                parentLayoutDashboard.alpha = 0.5f
                activity?.window!!.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            } else {
                parentLayoutDashboard.alpha = 1f
                activity?.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }
    }


}