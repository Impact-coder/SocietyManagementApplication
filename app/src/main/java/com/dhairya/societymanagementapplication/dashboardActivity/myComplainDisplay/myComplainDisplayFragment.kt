package com.dhairya.societymanagementapplication.dashboardActivity.myComplainDisplay

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.dashboardActivity.complain.ComplainFragmentArgs
import com.dhairya.societymanagementapplication.dashboardActivity.complain.ComplainViewModel
import com.dhairya.societymanagementapplication.databinding.FragmentMyComplainDisplayBinding
import com.google.android.material.snackbar.Snackbar


class myComplainDisplayFragment : Fragment(R.layout.fragment_my_complain_display) {



    private val args by navArgs<ComplainFragmentArgs>()
    private val viewModel: myComplainDisplayViewModel by viewModels()

    private lateinit var binding: FragmentMyComplainDisplayBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val complainData = args.complain


        binding  = FragmentMyComplainDisplayBinding.bind(view)

        binding.apply {
//            setResponse(complainData)
            date.setText(complainData.complainDate)
            flatNo.setText(complainData.flatNO)
            complain.setText(complainData.complain)
            name.setText(complainData.memberName)
            subject.setText(complainData.complainSubject)
            response.setText(complainData.complainResponse)




        }


    }




}