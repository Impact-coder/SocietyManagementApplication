package com.dhairya.societymanagementapplication.authActivity.authfragments.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
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

        }
    }

}