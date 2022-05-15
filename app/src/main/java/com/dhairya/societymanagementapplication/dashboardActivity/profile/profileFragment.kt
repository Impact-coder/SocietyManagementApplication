package com.dhairya.societymanagementapplication.dashboardActivity.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.authActivity.AuthActivity
import com.dhairya.societymanagementapplication.data.profileData
import com.dhairya.societymanagementapplication.databinding.FragmentProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class profileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private val profile_data = FirebaseFirestore.getInstance().collection("profileData")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {
            val userName = profile_data.document(Firebase.auth.currentUser!!.uid).get()
                .await().toObject(profileData::class.java)!!

            setDetails(
                userName.fullName,
                userName.flatNo,
                userName.mobile,
                userName.ownershipStatus,
                userName.profileImg,
                userName.email
            )

        }

        binding = FragmentProfileBinding.bind(view)
        binding.apply {

            btnEdit.setOnClickListener {
                findNavController().navigate(profileFragmentDirections.actionProfileFragmentToEditProfileFragment())
            }

            btnLogout.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Logout")
                builder.setIcon(R.drawable.ic_logout)
                builder.setMessage("Are you Sure you want to logout")
                    .setPositiveButton("Yes") { dialogInterface, which ->
                        Intent(requireContext(), AuthActivity::class.java).also {
                            startActivity(it)
                            requireActivity().finish()
                        }
                    }
                    .setNeutralButton("Cancel") { dialogInterface, which ->

                    }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }


    }

    private fun setDetails(
        fullName: String,
        flatNo: String,
        mobile: String,
        ownershipStatus: String,
        profileImg: String,
        email: String
    ) {

        Glide.with(binding.profileimg.context)
            .load(profileImg)
            .centerCrop()
            .into(binding.profileimg)
        binding.txtProfileName.text = fullName
        binding.txtProfileAddress.text = flatNo
        binding.txtProfileMobile.text = mobile
        binding.txtProfileEmail.text = email
        binding.txtProfileResidentStatus.text = ownershipStatus

    }

}