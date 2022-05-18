package com.dhairya.societymanagementapplication.authActivity.authfragments.ui.createProfile

import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class createProfileViewModel constructor(
    private val state: SavedStateHandle // it's store all data of fragment in a bundle when app goes in onpause state
) : ViewModel() {

    //Live data events

    private val createprofileEventChannel = Channel<CreateProfileEvent>()
    val createProfileEvent = createprofileEventChannel.receiveAsFlow()
    private val auth = FirebaseAuth.getInstance()
    private val residents = FirebaseFirestore.getInstance().collection("residents")
    private val profileData = FirebaseFirestore.getInstance().collection("profileData")
    private val storage = Firebase.storage


    //fetch data from fregement
    var createprofilename = state.get<String>("createprofilename") ?: ""
        set(value) {
            field = value
            state.set("createprofilename", value)
        }

    var createprofilemobileno = state.get<String>("createprofilemobileno") ?: ""
        set(value) {
            field = value
            state.set("createprofilemobileno", value)
        }

    var createprofileemail = state.get<String>("createprofileemail") ?: ""
        set(value) {
            field = value
            state.set("createprofileemail", value)
        }

    var createprofileflatno = state.get<String>("createprofileflatno") ?: ""
        set(value) {
            field = value
            state.set("createprofileflatno", value)
        }


    fun createProfile(imgUri: Uri, roleStatus: String) {



        if(createprofilename.isEmpty())
        {
           val error = "Name can't be empty!!"
            showErrorMessage(error)
            return

        }
        else if(createprofilemobileno.isEmpty())
        {
            val error = "Mobile can't be empty!!"
            showErrorMessage(error)
            return
        }
        else if(createprofileemail.isEmpty())
        {
            val error = "Email can't be empty!!"
            showErrorMessage(error)
            return

        }
        else if(createprofileflatno.isEmpty())
        {
            val error = "Flat No can't be empty!!"
            showErrorMessage(error)
            return
        }
        else if(imgUri == null)
        {
            val error = "Please select the profile image!!"
            showErrorMessage(error)
            return
        }
        else if (createprofilemobileno.length != 10)
        {
            val error = "Please enter valid mobile no!!"
            showErrorMessage(error)
            return
        }
        else
        {
            viewModelScope.launch(Dispatchers.Main) {
                try {

                    if (auth.currentUser != null) {

                        val id = profileData.document().id

                        val postId = UUID.randomUUID().toString()
                        val imageUploadResult = storage.getReference(postId).putFile(imgUri).await()
                        val imageUrl =
                            imageUploadResult?.metadata?.reference?.downloadUrl?.await().toString()

                        val profiledata = profileData(
                            pid = id,
                            memberid = Firebase.auth.currentUser!!.uid,
                            profileImg = imageUrl,
                            fullName = createprofilename,
                            mobile = createprofilemobileno,
                            email = createprofileemail,
                            flatNo = createprofileflatno,
                            ownershipStatus = roleStatus

                        )


                        profileData.document(auth.currentUser!!.uid).set(profiledata).await()
                        createprofileEventChannel.send(
                            CreateProfileEvent.NavigateBackWithResult(
                                com.dhairya.societymanagementapplication.dashboardActivity.AUTH_RESULT_OK
                            )
                        )

                    }

                } catch (e: Exception) {
                    showErrorMessage(e.message.toString())
                }

            }
        }


//            var photoUri =
//                Uri.parse("android.resource://com.dhairya.societymanagementapplication.authActivity.authfragments.ui.createProfile/$imgUri")



    }


    private fun showErrorMessage(text: String) = viewModelScope.launch {
        createprofileEventChannel.send(CreateProfileEvent.ShowErrorMessage(text))
    }


    sealed class CreateProfileEvent {
        data class ShowErrorMessage(val msg: String) : CreateProfileEvent()
        data class NavigateBackWithResult(val result: Int) : CreateProfileEvent()
    }

}