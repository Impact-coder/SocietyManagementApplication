package com.dhairya.societymanagementapplication.authActivity.authfragments.ui.forgotpassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.authActivity.authfragments.ui.login.loginViewModel
import com.dhairya.societymanagementapplication.databinding.FragmentForgotPasswordBinding
import com.dhairya.societymanagementapplication.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect

class forgotPasswordFragment : Fragment(R.layout.fragment_forgot_password) {

    private val viewModel: forgotPasswordViewModel by viewModels()
    private lateinit var binding: FragmentForgotPasswordBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding = FragmentForgotPasswordBinding.bind(view)
        binding.apply {

            forgotPasswordEmail.setText(viewModel.email)

            forgotPasswordEmail.addTextChangedListener {
                viewModel.email = it.toString()
            }

            btnForgotPassword.setOnClickListener {
                viewModel.forgotPassword()

            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.forgotPasswordEvent.collect { events ->
                    when (events) {
                        is forgotPasswordViewModel.ForgotPasswordEvent.NavigateBackWithResult -> {
                            Snackbar.make(
                                requireView(),
                                "Password reset link sent to your email",
                                Snackbar.LENGTH_LONG
                            ).show()
                            findNavController().navigate(forgotPasswordFragmentDirections.actionForgotPasswordFragmentToForgotPasswordConfirmationFragment())
                        }
                        is forgotPasswordViewModel.ForgotPasswordEvent.ShowErrorMessage -> {

                            Snackbar.make(requireView(), events.msg, Snackbar.LENGTH_LONG).show()
                        }

                    }.exhaustive

                }

            }
        }


    }

}

val <T> T.exhaustive: T
    get() = this