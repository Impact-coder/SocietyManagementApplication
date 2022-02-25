package com.dhairya.societymanagementapplication.authActivity.authfragments.ui.forgotpassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.authActivity.authfragments.ui.login.loginViewModel
import com.dhairya.societymanagementapplication.databinding.FragmentForgotPasswordBinding
import com.dhairya.societymanagementapplication.databinding.FragmentLoginBinding

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

        }
    }

}