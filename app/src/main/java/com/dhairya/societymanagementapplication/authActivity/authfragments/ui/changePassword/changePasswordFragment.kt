package com.dhairya.societymanagementapplication.authActivity.authfragments.ui.changePassword

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.authActivity.authfragments.ui.login.exhaustive
import com.dhairya.societymanagementapplication.authActivity.authfragments.ui.login.loginFragmentDirections
import com.dhairya.societymanagementapplication.authActivity.authfragments.ui.login.loginViewModel
import com.dhairya.societymanagementapplication.dashboardActivity.DashboardActivity
import com.dhairya.societymanagementapplication.data.residentsData
import com.dhairya.societymanagementapplication.databinding.FragmentChangePasswordBinding
import com.dhairya.societymanagementapplication.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class changePasswordFragment : Fragment(R.layout.fragment_change_password) {
    private val viewModel: changePasswordViewModel by viewModels()
    private lateinit var binding: FragmentChangePasswordBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Toast.makeText(context, "this is changePassword!", Toast.LENGTH_SHORT).show()

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

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.changePasswordEvent.collect { events ->
                @Suppress("IMPLICIT_CAST_TO_ANY")
                when (events) {
                    is changePasswordViewModel.ChangePasswordEvent.NavigateBackWithResult -> {
                        findNavController().navigate(changePasswordFragmentDirections.actionChangePasswordFragmentToCreateProfileFragment())

                    }
                    is changePasswordViewModel.ChangePasswordEvent.ShowErrorMessage -> {
                        Snackbar.make(requireView(), events.msg, Snackbar.LENGTH_LONG).show()

                    }
                }.exhaustive
            }
        }
    }

}