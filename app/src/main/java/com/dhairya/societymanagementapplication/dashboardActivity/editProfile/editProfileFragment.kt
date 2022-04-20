package com.dhairya.societymanagementapplication.dashboardActivity.editProfile

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.authActivity.authfragments.ui.createProfile.createProfileViewModel
import com.dhairya.societymanagementapplication.data.profileData
import com.dhairya.societymanagementapplication.databinding.FragmentCreateProfileBinding
import com.dhairya.societymanagementapplication.databinding.FragmentEditProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class editProfileFragment : Fragment(R.layout.fragment_edit_profile) {


    private val viewModel: editProfileViewModel by viewModels()

    private lateinit var binding: FragmentEditProfileBinding
    private val profile_data = FirebaseFirestore.getInstance().collection("profileData")
    private var userName: MutableList<profileData> = mutableListOf()


    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>

    lateinit var statusRadio: String

    private val mPickImage = 1
    lateinit var imgUri: Uri
    private lateinit var mYourBitmap: Bitmap


    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity()
                .setAspectRatio(16, 9)
                .getIntent(context)

        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }

    }

    @RequiresApi(Build.VERSION_CODES.P)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)
        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) {
            it?.let { uri ->


                imgUri = uri
                val source = ImageDecoder.createSource(context?.contentResolver!!, uri)
                mYourBitmap = ImageDecoder.decodeBitmap(source)
                val resizedImg = Bitmap.createScaledBitmap(mYourBitmap, 300, 300, true)

                binding.btnEditPic.setImageBitmap(resizedImg)

            }
        }

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

        binding = FragmentEditProfileBinding.bind(view)

        binding.apply {
            btnBack.setOnClickListener {
                findNavController().navigate(editProfileFragmentDirections.actionEditProfileFragmentToProfileFragment())
            }

            radioGroup.setOnCheckedChangeListener { radioGroup, i ->
                statusRadio = if (radioButton1.id == i) "Resident Owner" else "Renting Apartment"
                Toast.makeText(context, statusRadio, Toast.LENGTH_SHORT).show()
            }

            editProfileName.setText(viewModel.editProfileName)
            editProfileMobileNo.setText(viewModel.editProfileMobileNumber)
            editProfileFlatNo.setText(viewModel.editProfileFlatNo)




            editProfileName.addTextChangedListener {
                viewModel.editProfileName = it.toString()
            }

            editProfileMobileNo.addTextChangedListener {
                viewModel.editProfileMobileNumber = it.toString()
            }



            editProfileFlatNo.addTextChangedListener {
                viewModel.editProfileFlatNo = it.toString()
            }

            btnEditPic.setOnClickListener {

                cropActivityResultLauncher.launch(null)

            }

            btnEditDone.setOnClickListener {

                if (editProfileName.text == null) {
                    Toast.makeText(context, "Name field cannot be empty", Toast.LENGTH_SHORT).show()
                } else if (editProfileMobileNo.text == null) {
                    Toast.makeText(
                        context,
                        "Mobile Number field cannot be empty",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (editProfileFlatNo.text == null) {
                    Toast.makeText(context, "Flat Number field cannot be empty", Toast.LENGTH_SHORT)
                        .show()
                } else if (imgUri == null) {
                    Toast.makeText(context, "Flat Number field cannot be empty", Toast.LENGTH_SHORT)
                        .show()
                } else {
                 //   viewModel.editProfile(imgUri, statusRadio)
                    viewModel.editProfile()
                }
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

        statusRadio = ownershipStatus
        binding.editProfileName.setText(fullName)
        binding.editProfileFlatNo.setText(flatNo)
        binding.editProfileMobileNo.setText(mobile)

        Glide.with(binding.btnEditPic.context)
            .load(profileImg)
            .centerCrop()
            .into(binding.btnEditPic)

        if (ownershipStatus == "Resident Owner") {
            binding.radioButton1.isSelected = true
            binding.radioButton2.isSelected = false
        } else if (ownershipStatus == "Renting Appartment") {

            binding.radioButton1.isSelected = false
            binding.radioButton2.isSelected = true

        }


    }
}

