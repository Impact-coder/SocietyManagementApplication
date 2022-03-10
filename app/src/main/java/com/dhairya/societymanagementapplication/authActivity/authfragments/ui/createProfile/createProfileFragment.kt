package com.dhairya.societymanagementapplication.authActivity.authfragments.ui.createProfile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.dashboardActivity.DashboardActivity
import com.dhairya.societymanagementapplication.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect


class createProfileFragment : Fragment(R.layout.fragment_create_profile) {

    private val viewModel: createProfileViewModel by viewModel()
    private lateinit var binding: FragmentLoginBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding = FragmentLoginBinding.bind(view)
        binding.apply {

            create.setText(viewModel.email)
            loginPassword.setText(viewModel.password)

            loginUsername.addTextChangedListener {
                viewModel.email = it.toString()
            }
            loginPassword.addTextChangedListener {
                viewModel.password = it.toString()
            }


            btnLogin.setOnClickListener {
                viewModel.login()
            }

            textForgotPassword.setOnClickListener {
//                findNavController().navigate(loginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
            }


        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

            viewModel.createProfileEvent.collect { events ->
                when (events) {
                    is createProfileViewModel.CreaterProfileEvent.NavigateBackWithResult -> {
                        Intent(requireContext(), DashboardActivity::class.java).also {
                            startActivity(it)
                            requireActivity().finish()
                        }
                    }
                    is createProfileViewModel.CreaterProfileEvent.ShowErrorMessage -> {
                        Snackbar.make(requireView(), events.msg, Snackbar.LENGTH_LONG).show()

                    }
                }.exhaustive
            }

        }

    }

}


val <T> T.exhaustive: T
    get() = this
