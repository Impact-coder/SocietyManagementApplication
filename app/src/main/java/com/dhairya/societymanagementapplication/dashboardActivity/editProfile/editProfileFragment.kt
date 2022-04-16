package com.dhairya.societymanagementapplication.dashboardActivity.editProfile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.data.profileData
import com.dhairya.societymanagementapplication.databinding.FragmentEditProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class editProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private lateinit var binding: FragmentEditProfileBinding
    private val profile_data = FirebaseFirestore.getInstance().collection("profileData")
    private var userName: MutableList<profileData> = mutableListOf()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {
            userName = profile_data.whereEqualTo("memberid", Firebase.auth.currentUser!!.uid).get()
                .await().toObjects(profileData::class.java)

            setDetails(
                userName[0].fullName,
                userName[0].flatNo,
                userName[0].mobile,
                userName[0].ownershipStatus,
                userName[0].profileImg,
            )

        }

        binding= FragmentEditProfileBinding.bind(view)

        binding.apply {
            btnBack.setOnClickListener {
                findNavController().navigate(editProfileFragmentDirections.actionEditProfileFragmentToProfileFragment())
            }
        }

    }

    private fun setDetails(
        fullName: String,
        flatNo: String,
        mobile: String,
        ownershipStatus: String,
        profileImg: String,
    ) {

        binding.editProfileName.setText(fullName)
        binding.editProfileFlatNo.setText(flatNo)
        binding.editProfileMobileNo.setText(mobile)
        if(ownershipStatus=="Resident Owner"){
            binding.radioButton1.isSelected=true
            binding.radioButton2.isSelected=false
        }
        else if(ownershipStatus=="Renting Appartment"){
            binding.radioButton1.isSelected=false
            binding.radioButton2.isSelected=true
        }

    }
}