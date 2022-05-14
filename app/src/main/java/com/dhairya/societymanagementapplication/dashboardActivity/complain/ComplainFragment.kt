package com.dhairya.societymanagementapplication.dashboardActivity.complain

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.dashboardActivity.complainsList.complainsListViewModel
import com.dhairya.societymanagementapplication.dashboardActivity.fieComplain.fileComplainViewModel
import com.dhairya.societymanagementapplication.databinding.FragmentComplainBinding
import com.dhairya.societymanagementapplication.databinding.FragmentComplainsListBinding


class ComplainFragment : Fragment(R.layout.fragment_complain) {
    private val args by navArgs<ComplainFragmentArgs>()
    private val viewModel: ComplainViewModel by viewModels()

    private lateinit var binding: FragmentComplainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val complainData = args.complain

        binding  = FragmentComplainBinding.bind(view)

        binding.apply {

            txtDate.setText(complainData.complainDate)
            txtFlatno.setText(complainData.flatNO)
            txtComplain.setText(complainData.complain)
            txtName.setText(complainData.memberName)
            txtSubject.setText(complainData.complainSubject)

            complainReply.setText(viewModel.complainreply)

            complainReply.addTextChangedListener {
                viewModel.complainreply = it.toString()
            }

            btnReply.setOnClickListener {
                viewModel.setComplainReply(complainData.cid)
            }

        }


    }


}