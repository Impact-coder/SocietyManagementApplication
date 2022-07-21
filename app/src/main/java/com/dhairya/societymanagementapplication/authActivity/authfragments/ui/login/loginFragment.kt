package com.dhairya.societymanagementapplication.authActivity.authfragments.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
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
import com.dhairya.societymanagementapplication.authActivity.AUTH_RESULT_OK
import com.dhairya.societymanagementapplication.dashboardActivity.DashboardActivity
import com.dhairya.societymanagementapplication.data.profileData
import com.dhairya.societymanagementapplication.data.residentsData
import com.dhairya.societymanagementapplication.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
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
    private val REQ_ONE_TAP = 101  // Can be any integer unique to the Activity
    private var showOneTapUI = true
    lateinit var task: Task<GoogleSignInAccount>


    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val resident = FirebaseFirestore.getInstance().collection("residents")
    private var resi_Data: MutableList<residentsData> = mutableListOf()
    var isPasswordReset: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val signInRequest = BeginSignInRequest.builder()
//            .setGoogleIdTokenRequestOptions(
//                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    // Your server's client ID, not your Android client ID.
//                    .setServerClientId(getString(R.string.your_web_client_id))
//                    // Only show accounts previously used to sign in.
//                    .setFilterByAuthorizedAccounts(true)
//                    .build())
//            .build()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        // [END config_signin]


        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]


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

            btnGoogleLogin.setOnClickListener {
                signIn()
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

                            resi_Data =
                                resident.whereEqualTo("uid", Firebase.auth.currentUser!!.uid).get()
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


    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    // [END on_start_check_user]


    // [START onactivityresult]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(context,e.toString() , Toast.LENGTH_SHORT).show()
                Log.w("Error101", "Google sign in failed", e)
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    Toast.makeText(context, "Login Successfully !!", Toast.LENGTH_SHORT).show()

                    CoroutineScope(Dispatchers.Main).launch {

                        showProgress(true)

                        resi_Data =
                            resident.whereEqualTo("uid", Firebase.auth.currentUser!!.uid).get()
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
                    }

//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(context, "Login Failed !!", Toast.LENGTH_SHORT).show()

//                    updateUI(null)
                }
            }
    }
    // [END auth_with_google]

    // [START signin]
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    // [END signin]

    private fun updateUI(user: FirebaseUser?) {

    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
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
