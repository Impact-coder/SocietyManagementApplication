package com.dhairya.societymanagementapplication.authActivity.authfragments.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
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
    lateinit var sharedPreferences: SharedPreferences

    private val resident = FirebaseFirestore.getInstance().collection("residents")
    private var resi_Data: MutableList<residentsData> = mutableListOf()
    var isPasswordReset:String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        sharedPreferences = this.activity!!.getSharedPreferences("myprefs", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//
//        editor.putString("isChangePassWord", "no")
//        editor.putString("isProfileCretaed","no")



        CoroutineScope(Dispatchers.Main).launch{

            resi_Data =  resident.whereEqualTo("uid", Firebase.auth.currentUser!!.uid).get()
                .await().toObjects(residentsData::class.java)

            isPasswordReset = resi_Data[0].password
        }


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
                when (events) {
                    is loginViewModel.LoginEvent.NavigateBackWithResult -> {

                        if (isPasswordReset.isEmpty())
                        {
                            findNavController().navigate(loginFragmentDirections.actionLoginFragmentToChangePasswordFragment())
                        }
                        else {
                            Intent(requireContext(), DashboardActivity::class.java).also {
                                startActivity(it)
                                requireActivity().finish()
                            }
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
