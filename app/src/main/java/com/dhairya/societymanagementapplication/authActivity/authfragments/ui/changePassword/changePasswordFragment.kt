package com.dhairya.societymanagementapplication.authActivity.authfragments.ui.changePassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.authActivity.authfragments.ui.login.loginViewModel
import com.dhairya.societymanagementapplication.databinding.FragmentChangePasswordBinding
import com.dhairya.societymanagementapplication.databinding.FragmentLoginBinding

class changePasswordFragment : Fragment() {
    private val viewModel: changePasswordViewModel by viewModels()
    private lateinit var binding: FragmentChangePasswordBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding = FragmentChangePasswordBinding.bind(view)
        binding.apply {

            oldPassEdittext.setText(viewModel.oldPassword)
            newPassEdittext.setText(viewModel.newPassword)
            confirmPassEdittext.setText(viewModel.confirmPassword)

            oldPassEdittext.addTextChangedListener {
                viewModel.oldPassword = it.toString()
            }

            newPassEdittext.addTextChangedListener {
                viewModel.newPassword = it.toString()
            }

           confirmPassEdittext.addTextChangedListener {
                viewModel.confirmPassword = it.toString()
            }

            btnChangePassword.setOnClickListener {

                viewModel.changePassword()

            }

        }
    }
}