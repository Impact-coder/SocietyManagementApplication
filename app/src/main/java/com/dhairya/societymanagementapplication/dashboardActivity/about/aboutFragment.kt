package com.dhairya.societymanagementapplication.dashboardActivity.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.databinding.FragmentAboutBinding
import com.dhairya.societymanagementapplication.databinding.FragmentAddMemberBinding

class aboutFragment : Fragment(R.layout.fragment_about) {

    private lateinit var binding: FragmentAboutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAboutBinding.bind(view)
        binding.apply {

            btnBack.setOnClickListener {
                findNavController().navigate(aboutFragmentDirections.actionAboutFragmentToDashBoardFragment())
            }

        }
    }

}

