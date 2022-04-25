package com.dhairya.societymanagementapplication.dashboardActivity.editProfile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhairya.societymanagementapplication.authActivity.authfragments.ui.createProfile.createProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

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
    var editProfileName  = state.get<String>("editprofilename") ?: ""
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


    fun editProfile() {

        viewModelScope.launch(Dispatchers.IO) {


        }


    }

    private fun showErrorMessage(text: String) = viewModelScope.launch {
        editprofileEventChannel.send(EditProfileEvent.ShowErrorMessage(text))
    }


    sealed class EditProfileEvent {
        data class ShowErrorMessage(val msg: String) : EditProfileEvent()
        data class NavigateBackWithResult(val result: Int) : EditProfileEvent()
    }

}