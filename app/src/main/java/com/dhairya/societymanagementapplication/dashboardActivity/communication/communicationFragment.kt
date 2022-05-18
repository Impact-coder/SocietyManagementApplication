package com.dhairya.societymanagementapplication.dashboardActivity.communication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.dashboardActivity.rules.rulesFragmentDirections
import com.dhairya.societymanagementapplication.databinding.FragmentCommunicationBinding
import com.dhairya.societymanagementapplication.databinding.FragmentRulesBinding

class communicationFragment : Fragment(R.layout.fragment_communication) {

    private lateinit var binding: FragmentCommunicationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCommunicationBinding.bind(view)
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().navigate(communicationFragmentDirections.actionCommunicationFragmentToDashBoardFragment())
            }
        }
    }

}