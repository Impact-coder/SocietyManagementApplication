package com.dhairya.societymanagementapplication.dashboardActivity.maintenanceInvoice

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.service.autofill.UserData
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.dashboardActivity.addMember.addMemberViewModel
import com.dhairya.societymanagementapplication.data.profileData
import com.dhairya.societymanagementapplication.databinding.FragmentAddMemberBinding
import com.dhairya.societymanagementapplication.databinding.FragmentMaintenanceInvoiceBinding
import com.dhairya.societymanagementapplication.paymaintenance.PayMaintenance
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.json.JSONObject


class maintenanceInvoiceFragment : Fragment(R.layout.fragment_maintenance_invoice) {

    private val viewModel: maintenanceinvoiceViewModel by viewModels()
    val profile_data = FirebaseFirestore.getInstance().collection("profileData")
    lateinit var userData: profileData
    private lateinit var binding: FragmentMaintenanceInvoiceBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        activity?.window?.statusBarColor = Color.parseColor("#A8B3BC")

        CoroutineScope(Dispatchers.Main).launch {

            userData = profile_data.document(Firebase.auth.currentUser!!.uid).get().await()
                .toObject(profileData::class.java)!!
        }


        binding = FragmentMaintenanceInvoiceBinding.bind(view)
        binding.apply {


            invoiceName.text = userData.fullName
            invoiceFlatNo.text = userData.flatNo
            invoiceMobile.text = userData.mobile

            btnPay.setOnClickListener {
                Intent(requireContext(), PayMaintenance::class.java).apply {
                    putExtra("Maintenance Amount", invoiceMaintenanceAmount.text)
                    putExtra("email", userData.email)
                    putExtra("mobile", userData.mobile)
                    startActivity(this)
                }

            }

        }

    }

}
