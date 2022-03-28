package com.dhairya.societymanagementapplication.authActivity.authfragments.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.dashboardActivity.DashboardActivity
import com.dhairya.societymanagementapplication.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect

class loginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel: loginViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding = FragmentLoginBinding.bind(view)
        binding.apply {


            loginUsername.setText(viewModel.email)
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
                findNavController().navigate(loginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
            }


        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.loginEvent.collect { events ->
                when(events){
                    is loginViewModel.LoginEvent.NavigateBackWithResult -> {
                            Intent(requireContext(), DashboardActivity ::class.java).also {
                                startActivity(it)
                                requireActivity().finish()
                            }
                    }
                    is loginViewModel.LoginEvent.ShowErrorMessage -> {
                        Snackbar.make(requireView(), events.msg, Snackbar.LENGTH_LONG).show()

                    }
                }.exhaustive

            }

        }

    }


}

val <T> T.exhaustive: T
    get() = this
