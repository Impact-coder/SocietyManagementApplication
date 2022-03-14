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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.dashboardActivity.DashboardActivity
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

    private val mPickImage = 1
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

            cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract){
                it?.let { uri ->



                    val source = ImageDecoder.createSource(context?.contentResolver!!, uri)
                    mYourBitmap = ImageDecoder.decodeBitmap(source)
                    val resized = Bitmap.createScaledBitmap(mYourBitmap, 300, 300, true)

                    btnChoosePic.setImageBitmap(resized)

                }
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



            btnCreateProfile.setOnClickListener {
                viewModel.createProfile()
            }

            btnChoosePic.setOnClickListener {


                cropActivityResultLauncher.launch(null)


            }


        }

    }

}


val <T> T.exhaustive: T
    get() = this

