package com.dhairya.societymanagementapplication.authActivity.authfragments.ui.login

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.databinding.ActivityAddingMemberBinding.bind
import com.dhairya.societymanagementapplication.databinding.FragmentLoginBinding

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
//                   findNavController().navigate(loginFragmentDirections.actionGlobalLoginFragment())

                   Toast.makeText(context, "Login Done", Toast.LENGTH_SHORT).show()
               }
           }


    }

}