package com.dhairya.societymanagementapplication.authActivity.authfragments.ui.createProfile

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhairya.societymanagementapplication.data.profileData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class createProfileViewModel constructor(
    private val state: SavedStateHandle // it's store all data of fragment in a bundle when app goes in onpause state
) : ViewModel() {


    //Live data events

    private val createprofileEventChannel = Channel<CreateProfileEvent>()
    val createProfileEvent = createprofileEventChannel.receiveAsFlow()



    private val auth = FirebaseAuth.getInstance()
    private val residents = FirebaseFirestore.getInstance().collection("residents")
    private val profileData = FirebaseFirestore.getInstance().collection("profileData")
//    private val storage = Firebase.storage

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


    fun createProfile(imgUri: String, roleStatus: String) {


        if (createprofilename.isBlank() || createprofileflatno.isBlank() || createprofilemobileno.isBlank() || createprofileemail.isBlank()) {
            val error = "The field must not be empty"
            showErrorMessage(error)
            return
        } else {
            var photoUri =
                Uri.parse("android.resource://com.dhairya.societymanagementapplication.authActivity.authfragments.ui.createProfile/$imgUri")

            viewModelScope.launch(Dispatchers.IO) {
                try {

                    if (auth.currentUser != null) {
                        val id = profileData.document().id

//                        val imageUploadResult = storage.get


                        val profiledata = profileData(
                            pid = id,
                            memberid = Firebase.auth.currentUser!!.uid,
                            profileImg = "",
                            fullName = createprofilename,
                            mobile = createprofilemobileno,
                            email = createprofileemail,
                            flatNo = createprofileflatno,
                            ownershipStatus = roleStatus

                        )

                        profileData.document(id).set(profiledata).await()
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
    }


    private fun showErrorMessage(text: String) = viewModelScope.launch {
        createprofileEventChannel.send(CreateProfileEvent.ShowErrorMessage(text))
    }


    sealed class CreateProfileEvent {
        data class ShowErrorMessage(val msg: String) : CreateProfileEvent()
        data class NavigateBackWithResult(val result: Int) : CreateProfileEvent()
    }

}