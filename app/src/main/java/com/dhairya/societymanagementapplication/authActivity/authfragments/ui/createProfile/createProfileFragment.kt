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
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
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
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.flow.collect


class createProfileFragment : Fragment(R.layout.fragment_create_profile) {

    private val viewModel: createProfileViewModel by viewModels()
    private lateinit var binding: FragmentCreateProfileBinding

    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>

    var statusRadio: String = "Resident Owner"

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

        binding = FragmentCreateProfileBinding.bind(view)
        binding.apply {

            cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) {
                it?.let { uri ->


                    imgUri = uri
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

            btnCreateProfile.setOnClickListener{

                try {
                    //                if(imgUri == null)
//                {
//                    Toast.makeText(context, "Flat number field cannot be empty", Toast.LENGTH_SHORT).show()
//                }
//                else
//                {
                    showProgress(true)
                    viewModel.createProfile(imgUri, statusRadio)
                    //}

                } catch (e: Exception) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
                    showProgress(false)

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
                            showProgress(false)
                            Intent(requireContext(), DashboardActivity::class.java).also {
                                startActivity(it)
                                requireActivity().finish()
                            }
                        }
                        is createProfileViewModel.CreateProfileEvent.ShowErrorMessage -> {
                            Snackbar.make(requireView(), events.msg, Snackbar.LENGTH_LONG)
                                .show()
                            showProgress(false)
                        }
                    }.exhaustive
                }
            }
        }


    }

    private fun showProgress(bool: Boolean) {
        binding.apply {
            animationView.isVisible = bool
            if (bool) {
                parentLayoutCreateProfile.alpha = 0.5f
                activity?.window!!.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            } else {
                parentLayoutCreateProfile.alpha = 1f
                activity?.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }
    }

}

val <T> T.exhaustive: T
    get() = this

