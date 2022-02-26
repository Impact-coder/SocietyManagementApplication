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

    //Object of view model
    private val viewModel: loginViewModel by viewModels()
    //view binding
    private lateinit var binding: FragmentLoginBinding

    // oncreate activity of fragement lifecycle
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //init obj
        binding = FragmentLoginBinding.bind(view)

        binding.apply {

               loginUsername.setText(viewModel.email)
               loginPassword.setText(viewModel.password)

                //if any changes is feilds the automatically store in viewmodel
               loginUsername.addTextChangedListener {
                   viewModel.email = it.toString()
               }
               loginPassword.addTextChangedListener {
                   viewModel.password = it.toString()
               }


               btnLogin.setOnClickListener {
                   viewModel.login()

                   // travle one fragement t another fragement
                   findNavController().navigate(loginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
                   Toast.makeText(context, "Login Done", Toast.LENGTH_SHORT).show()
               }
           }


    }

}