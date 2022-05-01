package com.dhairya.societymanagementapplication.dashboardActivity.editProfile

import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhairya.societymanagementapplication.authActivity.authfragments.ui.createProfile.createProfileViewModel
import com.dhairya.societymanagementapplication.dashboardActivity.AUTH_RESULT_OK
import com.dhairya.societymanagementapplication.dashboardActivity.addMember.addMemberViewModel
import com.dhairya.societymanagementapplication.data.profileData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.*

class editProfileViewModel(
    private val state: SavedStateHandle // it's store all data of fragment in a bundle when app goes in onpause state
) : ViewModel() {


    //Live data events

    private val editprofileEventChannel = Channel<EditProfileEvent>()
    val editProfileEvent = editprofileEventChannel.receiveAsFlow()
    var user = Firebase.auth.currentUser
    private val auth = FirebaseAuth.getInstance()
    private val residents = FirebaseFirestore.getInstance().collection("residents")
    private val profileData = FirebaseFirestore.getInstance().collection("profileData")
    private val storage = Firebase.storage

    //fetch data from fregement
    var editProfileName = state.get<String>("editprofilename") ?: ""
        set(value) {
            field = value
            state.set("editprofilename", value)
        }

    var editProfileMobileNumber = state.get<String>("editmobilenumber") ?: ""
        set(value) {
            field = value
            state.set("editmobilenumber", value)
        }


    var editProfileFlatNo = state.get<String>("editprofilrflaatno") ?: ""
        set(value) {
            field = value
            state.set("editprofilrflaatno", value)
        }


    fun editProfile(statusRadio: String) {

        if (editProfileName.isEmpty()) {
            var error = "Full name cnn not be empty!!"
            showErrorMessage(error)
        } else if (editProfileMobileNumber.isEmpty()) {
            var error = "Mobile number cnn not be empty!!"
            showErrorMessage(error)
        } else if (editProfileFlatNo.isEmpty()) {
            var error = "Flat no cnn not be empty!!"
            showErrorMessage(error)
        } else {
            var name = editProfileName
            var flatNo = editProfileFlatNo
            var mobile = editProfileMobileNumber


            viewModelScope.launch(Dispatchers.IO) {

                profileData.document(auth.currentUser!!.uid).update(
                    "flatNo", flatNo,
                    "fullName", name,
                    "mobile", mobile,
                    "ownershipStatus", statusRadio
                )

                editprofileEventChannel.send(
                    editProfileViewModel.EditProfileEvent.NavigateBackWithResult(
                        AUTH_RESULT_OK
                    )
                )


            }

        }


    }


    private fun showErrorMessage(text: String) = viewModelScope.launch {
        editprofileEventChannel.send(EditProfileEvent.ShowErrorMessage(text))
    }

    fun updateProfile(imgUri: Uri, statusRadio: String) {

        if (editProfileName.isEmpty()) {
            var error = "Full name cnn not be empty!!"
            showErrorMessage(error)
        } else if (editProfileMobileNumber.isEmpty()) {
            var error = "Mobile number cnn not be empty!!"
            showErrorMessage(error)
        } else if (editProfileFlatNo.isEmpty()) {
            var error = "Flat no cnn not be empty!!"
            showErrorMessage(error)
        } else if (imgUri == null) {
            val error = "Please select the profile image!!"
            showErrorMessage(error)
            return
        } else {
            var name = editProfileName
            var flatNo = editProfileFlatNo
            var mobile = editProfileMobileNumber

            try {
                viewModelScope.launch(Dispatchers.IO) {

                    if (auth.currentUser != null) {

                        val id = profileData.document().id
                        val postId = UUID.randomUUID().toString()
                        val imageUploadResult = storage.getReference(postId).putFile(imgUri).await()
                        val imageUrl =
                            imageUploadResult?.metadata?.reference?.downloadUrl?.await().toString()


                        profileData.document(auth.currentUser!!.uid).update(
                            "flatNo", flatNo,
                            "fullName", name,
                            "mobile", mobile,
                            "ownershipStatus", statusRadio,
                            "profileImg", imageUrl
                        )

                        editprofileEventChannel.send(
                            editProfileViewModel.EditProfileEvent.NavigateBackWithResult(
                                AUTH_RESULT_OK
                            )
                        )


                    }
                }

            } catch (e: Exception) {
                showErrorMessage(e.message.toString())
            }

        }

    }


    sealed class EditProfileEvent {
        data class ShowErrorMessage(val msg: String) : EditProfileEvent()
        data class NavigateBackWithResult(val result: Int) : EditProfileEvent()
    }

}