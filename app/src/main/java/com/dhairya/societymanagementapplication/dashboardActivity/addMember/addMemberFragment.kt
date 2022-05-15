package com.dhairya.societymanagementapplication.dashboardActivity.addMember

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.authActivity.AuthActivity
import com.dhairya.societymanagementapplication.authActivity.authfragments.ui.login.exhaustive
import com.dhairya.societymanagementapplication.authActivity.authfragments.ui.login.loginViewModel
import com.dhairya.societymanagementapplication.dashboardActivity.DashboardActivity
import com.dhairya.societymanagementapplication.databinding.FragmentAddMemberBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collect

class addMemberFragment : Fragment(R.layout.fragment_add_member) {
    private val viewModel: addMemberViewModel by viewModels()
    private lateinit var binding: FragmentAddMemberBinding

    var radio: String = "member"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding = FragmentAddMemberBinding.bind(view)
        binding.apply {

            radioGroupMemberRoles.setOnCheckedChangeListener { radioGroup, i ->
                radio = if (radioButton1.id == i) "member" else "treasurer"

//                Snackbar.make(requireView(), radio, Snackbar.LENGTH_LONG).show()
            }

            addMemberEmailEdittext.setText(viewModel.addMemberEmailEdittext)
            addMemberFlatnoEdittext.setText(viewModel.addMemberFlatnoEdittext)

            addMemberEmailEdittext.addTextChangedListener {
                viewModel.addMemberEmailEdittext = it.toString()

            }

            addMemberFlatnoEdittext.addTextChangedListener {
                viewModel.addMemberFlatnoEdittext = it.toString()

            }

            btnAddMember.setOnClickListener {


                viewModel.addMember(radio)

            }


            btnEditDone.setOnClickListener {

                addMemberEmailEdittext.setText("")
                addMemberFlatnoEdittext.setText("")
                FirebaseAuth.getInstance().signOut()
                Intent(requireActivity(), AuthActivity::class.java).also {
                    startActivity(it)
                    requireActivity().finish()
                }

            }

            btnBack.setOnClickListener {
                findNavController().navigate(addMemberFragmentDirections.actionAddMemberFragmentToDashBoardFragment())
            }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addMemberEvent.collect { events ->
                when (events) {
                    is addMemberViewModel.AddMemberEvent.NavigateBackWithResult -> {
                        Snackbar.make(
                            requireView(),
                            "Members added Successfully!!",
                            Snackbar.LENGTH_LONG
                        ).show()

                        binding.addMemberEmailEdittext.setText("")
                        binding.addMemberFlatnoEdittext.setText("")
                        binding.radioButton1.isSelected = true
                        binding.radioButton2.isSelected = false


                    }
                    is addMemberViewModel.AddMemberEvent.ShowErrorMessage -> {
                        Snackbar.make(requireView(), events.msg, Snackbar.LENGTH_LONG).show()
                    }
                }.exhaustive
            }

        }
    }
}

val <T> T.exhaustive: T
    get() = this
