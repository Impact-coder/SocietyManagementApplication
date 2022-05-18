package com.dhairya.societymanagementapplication.dashboardActivity.rules

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.dashboardActivity.fieComplain.fileComplainViewModel
import com.dhairya.societymanagementapplication.databinding.FragmentFileComplainBinding
import com.dhairya.societymanagementapplication.databinding.FragmentRulesBinding

class rulesFragment : Fragment(R.layout.fragment_rules) {

    private lateinit var binding: FragmentRulesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRulesBinding.bind(view)
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().navigate(rulesFragmentDirections.actionRulesFragmentToDashBoardFragment())
            }
        }
    }

}