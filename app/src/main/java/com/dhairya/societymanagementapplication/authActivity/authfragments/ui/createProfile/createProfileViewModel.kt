package com.dhairya.societymanagementapplication.authActivity.authfragments.ui.createProfile

import android.app.Activity
import android.app.appsearch.AppSearchResult.RESULT_OK
import android.content.Intent
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhairya.societymanagementapplication.authActivity.AUTH_RESULT_OK
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import androidx.core.app.ActivityCompat.startActivityForResult
//import sun.font.LayoutPathImpl.getPath







class createProfileViewModel constructor(
    private val state: SavedStateHandle // it's store all data of fragment in a bundle when app goes in onpause state
) : ViewModel() {
    private val SELECT_PICTURE = 1
    //Live data events

    private val createProfileEventChannel = Channel<CreaterProfileEvent>()
    val createProfileEvent = createProfileEventChannel.receiveAsFlow()


    private val auth = FirebaseAuth.getInstance()

    //fetch data from fregement
    var createprofilename = state.get<String>("createprofilename") ?: ""
        set(value) {
            field = value
            state.set("createprofilename", value)
        }

    var createprofileemail = state.get<String>("createprofileemail") ?: ""
        set(value) {
            field = value
            state.set("createprofileemail", value)
        }

    var createprofilemobileno = state.get<String>("createprofilemobileno") ?: ""
        set(value) {
            field = value
            state.set("createprofilemobileno", value)
        }

    var createprofileflatno = state.get<String>("createprofileflatno") ?: ""
        set(value) {
            field = value
            state.set("createprofileflatno", value)
        }



    //fun for login
    fun createProfile() {
        if (createprofilename.isBlank() || createprofileemail.isBlank() || createprofilemobileno.isBlank() || createprofileflatno.isBlank()) {
            val error = "The field must not be empty!!"
            showErrorMessage(error)
            return
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                try {
//                    auth.signInWithEmailAndPassword(email, password).await()
//                    createProfileEventChannel.send(CreaterProfileEvent.NavigateBackWithResult(AUTH_RESULT_OK))
                } catch (e: Exception) {
                    showErrorMessage(e.message.toString())
                }
            }
        }


    }




    private fun showErrorMessage(text: String) = viewModelScope.launch {
        createProfileEventChannel.send(CreaterProfileEvent.ShowErrorMessage(text))
    }

//    private fun openGalleryForImage() {
//
//    }
//
//    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        if (requestCode ==2 && resultCode == RESULT_OK && data != null){
//
//            imageUri = data.getData();
////            imageView.setImageURI(imageUri);
//
//        }
//    }

    fun choosePick() {

    }

    sealed class CreaterProfileEvent {
        data class ShowErrorMessage(val msg: String) : CreaterProfileEvent()
        data class NavigateBackWithResult(val result: Int) : CreaterProfileEvent()
    }

}