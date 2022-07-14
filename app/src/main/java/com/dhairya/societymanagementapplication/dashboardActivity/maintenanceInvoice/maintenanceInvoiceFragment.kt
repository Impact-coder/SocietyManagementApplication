package com.dhairya.societymanagementapplication.dashboardActivity.maintenanceInvoice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.dashboardActivity.addMember.addMemberViewModel
import com.dhairya.societymanagementapplication.databinding.FragmentAddMemberBinding
import com.dhairya.societymanagementapplication.databinding.FragmentMaintenanceInvoiceBinding


class maintenanceInvoiceFragment : Fragment(R.layout.fragment_maintenance_invoice) {

    private val viewModel: maintenanceinvoiceViewModel by viewModels()
    private lateinit var binding: FragmentMaintenanceInvoiceBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMaintenanceInvoiceBinding.bind(view)
        binding.apply {

            btnPay.setOnClickListener {
                viewModel.PayMaintenance()
            }


        }


    }
}