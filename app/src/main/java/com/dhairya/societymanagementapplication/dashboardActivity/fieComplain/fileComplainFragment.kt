package com.dhairya.societymanagementapplication.dashboardActivity.fieComplain

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.authActivity.authfragments.ui.login.exhaustive
import com.dhairya.societymanagementapplication.dashboardActivity.addMember.addMemberFragmentDirections
import com.dhairya.societymanagementapplication.dashboardActivity.addTransaction.addTransactionViewModel
import com.dhairya.societymanagementapplication.dashboardActivity.notice.noticeViewModel
import com.dhairya.societymanagementapplication.databinding.FragmentAddTransactionBinding
import com.dhairya.societymanagementapplication.databinding.FragmentFileComplainBinding
import com.dhairya.societymanagementapplication.databinding.FragmentNoticeBinding
import com.google.android.material.snackbar.Snackbar


class fileComplainFragment : Fragment(R.layout.fragment_file_complain) {

    private val viewModel: fileComplainViewModel by viewModels()
    private lateinit var binding: FragmentFileComplainBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFileComplainBinding.bind(view)
        binding.apply {

            complainMessage.setText(viewModel.complainMessage)
            complainSubject.setText(viewModel.complainSubject)


            complainSubject.addTextChangedListener {
                viewModel.complainSubject = it.toString()

            }

            complainMessage.addTextChangedListener {
                viewModel.complainMessage = it.toString()

            }

            btnSendComplain.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    viewModel.fileComplain()
                }
            }

            btnBack.setOnClickListener {
                findNavController().navigate(fileComplainFragmentDirections.actionFileComplainToDashBoardFragment())
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.fileComplainEvent.collect { events ->
                    when (events) {

                        is fileComplainViewModel.FileComplainEvent.NavigateBackWithResult -> {
                            Toast.makeText(
                                context,
                                "Complain Filed Successfully!!",
                                Toast.LENGTH_SHORT
                            ).show()
//                            events.list

                            binding.complainSubject.setText("")
                            binding.complainMessage.setText("")

                        }

                    is fileComplainViewModel.FileComplainEvent.ShowErrorMessage -> {
                    Snackbar.make(requireView(), events.msg, Snackbar.LENGTH_LONG).show()

                }
                }.exhaustive
            }

        }
    }

}




}