package com.dhairya.societymanagementapplication.authActivity.authfragments.ui.createProfile

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
import androidx.navigation.fragment.findNavController
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.authActivity.authfragments.ui.login.exhaustive
import com.dhairya.societymanagementapplication.dashboardActivity.DashboardActivity
import com.dhairya.societymanagementapplication.dashboardActivity.addMember.addMemberViewModel
import com.dhairya.societymanagementapplication.databinding.FragmentCreateProfileBinding
import com.dhairya.societymanagementapplication.databinding.FragmentCreateProfileBinding.*
import com.dhairya.societymanagementapplication.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.coroutines.flow.collect


class createProfileFragment : Fragment(R.layout.fragment_create_profile) {

    private val viewModel: createProfileViewModel by viewModels()
    private lateinit var binding: FragmentCreateProfileBinding

    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>

    lateinit var statusRadio: String

    private val mPickImage = 1
    lateinit var imgUri: String
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

        binding = FragmentCreateProfileBinding.bind(view)
        binding.apply {

            cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) {
                it?.let { uri ->


                    imgUri = uri.toString()
                    val source = ImageDecoder.createSource(context?.contentResolver!!, uri)
                    mYourBitmap = ImageDecoder.decodeBitmap(source)
                    val resizedImg = Bitmap.createScaledBitmap(mYourBitmap, 300, 300, true)

                    btnChoosePic.setImageBitmap(resizedImg)

                }
            }

            radioGroup.setOnCheckedChangeListener { radioGroup, i ->
                statusRadio = if (radioButton1.id == i) "Resident Owner" else "Renting Apartment"
                Toast.makeText(context, statusRadio, Toast.LENGTH_SHORT).show()
            }

            createProfileName.setText(viewModel.createprofilename)
            createProfileMobileNo.setText(viewModel.createprofilemobileno)
            createProfileEmail.setText(viewModel.createprofileemail)
            createProfileFlatNo.setText(viewModel.createprofileflatno)




            createProfileName.addTextChangedListener {
                viewModel.createprofilename = it.toString()
            }

            createProfileMobileNo.addTextChangedListener {
                viewModel.createprofilemobileno = it.toString()
            }

            createProfileEmail.addTextChangedListener {
                viewModel.createprofileemail = it.toString()
            }

            createProfileFlatNo.addTextChangedListener {
                viewModel.createprofileflatno = it.toString()
            }

            btnChoosePic.setOnClickListener {

                cropActivityResultLauncher.launch(null)

            }

            btnCreateProfile.setOnClickListener {

                if(createProfileName.text == null)
                {
                    Toast.makeText(context, "Name field cannot be empty", Toast.LENGTH_SHORT).show()
                }
                else if(createProfileMobileNo.text == null)
                {
                    Toast.makeText(context, "Mobile Number field cannot be empty", Toast.LENGTH_SHORT).show()
                }
                else if(createProfileEmail.text == null)
                {
                    Toast.makeText(context, "Email field cannot be empty", Toast.LENGTH_SHORT).show()
                }
                else if(createProfileFlatNo.text == null)
                {
                    Toast.makeText(context, "Flat Number field cannot be empty", Toast.LENGTH_SHORT).show()
                }
                else if(imgUri == null)
                {
                    Toast.makeText(context, "Flat Number field cannot be empty", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    viewModel.createProfile(imgUri, statusRadio)
                }

            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.createProfileEvent.collect { events ->
                    when (events) {
                        is createProfileViewModel.CreateProfileEvent.NavigateBackWithResult -> {
                            Snackbar.make(
                                requireView(),
                                "Profile Created Successfully!!",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                        is createProfileViewModel.CreateProfileEvent.ShowErrorMessage -> {
                            Snackbar.make(requireView(), events.msg, Snackbar.LENGTH_LONG)
                                .show()
                        }
                    }.exhaustive

                }


            }

        }

    }
}


val <T> T.exhaustive: T
    get() = this

