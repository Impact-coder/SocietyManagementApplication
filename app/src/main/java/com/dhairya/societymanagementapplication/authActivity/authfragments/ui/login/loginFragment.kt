    package com.dhairya.societymanagementapplication.authActivity.authfragments.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.dashboardActivity.DashboardActivity
import com.dhairya.societymanagementapplication.data.profileData
import com.dhairya.societymanagementapplication.data.residentsData
import com.dhairya.societymanagementapplication.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class loginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel: loginViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding

    private val resident = FirebaseFirestore.getInstance().collection("residents")
    private var resi_Data: MutableList<residentsData> = mutableListOf()
    var isPasswordReset: String = ""

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
                showProgress(true)
                viewModel.login()
            }

            textForgotPassword.setOnClickListener {
                findNavController().navigate(loginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
            }


        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.loginEvent.collect { events ->
                @Suppress("IMPLICIT_CAST_TO_ANY")
                when (events) {
                    is loginViewModel.LoginEvent.NavigateBackWithResult -> {

                        CoroutineScope(Dispatchers.Main).launch {

                            showProgress(true)

                            resi_Data = resident.whereEqualTo("uid", Firebase.auth.currentUser!!.uid).get()
                                .await().toObjects(residentsData::class.java)

                            isPasswordReset = resi_Data[0].password

                          //  Toast.makeText(requireContext(), isPasswordReset, Toast.LENGTH_SHORT).show()

                            if (isPasswordReset == "") {
                                findNavController().navigate(loginFragmentDirections.actionLoginFragmentToChangePasswordFragment())
                            } else {
                                Intent(requireContext(), DashboardActivity::class.java).also {
                                    startActivity(it)
                                    requireActivity().finish()
                                }
                            }
                            showProgress(false)
                        }

                    }

                    is loginViewModel.LoginEvent.ShowErrorMessage -> {
                        Snackbar.make(requireView(), events.msg, Snackbar.LENGTH_LONG).show()
                        showProgress(false)
                    }
                }.exhaustive
            }
        }
    }

    private fun showProgress(bool: Boolean) {
        binding.apply {
            animationView.isVisible = bool
            if (bool) {
                parentLayoutLogin.alpha = 0.5f
                activity?.window!!.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            } else {
                parentLayoutLogin.alpha = 1f
                activity?.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }
    }

}

val <T> T.exhaustive: T
    get() = this
