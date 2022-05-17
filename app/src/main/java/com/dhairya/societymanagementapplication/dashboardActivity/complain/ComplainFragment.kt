package com.dhairya.societymanagementapplication.dashboardActivity.complain

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.authActivity.authfragments.ui.login.exhaustive
import com.dhairya.societymanagementapplication.data.complainData
import com.dhairya.societymanagementapplication.databinding.FragmentComplainBinding
import com.google.android.material.snackbar.Snackbar


class ComplainFragment : Fragment(R.layout.fragment_complain) {
    private val args by navArgs<ComplainFragmentArgs>()
    private val viewModel: ComplainViewModel by viewModels()

    private lateinit var binding: FragmentComplainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val complainData = args.complain


        binding  = FragmentComplainBinding.bind(view)

        binding.apply {
//            setResponse(complainData)
            date.setText(complainData.complainDate)
            flatNo.setText(complainData.flatNO)
            complain.setText(complainData.complain)
            name.setText(complainData.memberName)
            subject.setText(complainData.complainSubject)
            response.setText(complainData.complainResponse)

            complainReply.setText(viewModel.complainreply)
            response.setText(viewModel.complainreponse)


            complainReply.addTextChangedListener {
                viewModel.complainreply = it.toString()
            }

            response.addTextChangedListener{
                viewModel.complainreponse = it.toString()
            }

            btnReply.setOnClickListener {
                viewModel.setComplainReply(complainData.cid)
            }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.complainEvent.collect { events ->
                when (events) {
                    is ComplainViewModel.ComplainEvent.NavigateBackWithResult -> {
                        Toast.makeText(
                            context,
                            "Replied Successfully!!",
                            Toast.LENGTH_SHORT
                        ).show()

                        binding.complainReply.setText("")

                    }
                    is ComplainViewModel.ComplainEvent.ShowErrorMessage -> {
                        Snackbar.make(requireView(), events.msg, Snackbar.LENGTH_LONG).show()

                    }
                }.exhaustive
            }

        }


    }

    private fun setResponse(complainData: complainData) {

        binding.response.setText(complainData.complainResponse)


    }


}