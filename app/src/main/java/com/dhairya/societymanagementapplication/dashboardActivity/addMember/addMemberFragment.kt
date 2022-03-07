package com.dhairya.societymanagementapplication.dashboardActivity.addMember

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dhairya.societymanagementapplication.databinding.FragmentAddMemberBinding


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class addMemberFragment : Fragment() {
    private val viewModel: addMemberViewModel by viewModels()
    private lateinit var binding: FragmentAddMemberBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding = FragmentAddMemberBinding.bind(view)
        binding.apply {

           addMemberEmailEdittext.setText(viewModel.addMemberEmailEdittext)
           addMemberFlatnoEdittext.setText(viewModel.addMemberFlatnoEdittext)
//            confirmPassEdittext.setText(viewModel.confirmPassword)

            addMemberEmailEdittext.addTextChangedListener {
                viewModel.addMemberEmailEdittext = it.toString()
            }

            addMemberFlatnoEdittext.addTextChangedListener {
                viewModel.addMemberFlatnoEdittext = it.toString()
            }

            btnAddMember.setOnClickListener {

                viewModel.addMember()

            }

            btnDone.setOnClickListener {
                findNavController().navigate(addMemberFragmentDirections.actionAddMemberFragmentToDashBoardFragment())
            }

        }
    }

}