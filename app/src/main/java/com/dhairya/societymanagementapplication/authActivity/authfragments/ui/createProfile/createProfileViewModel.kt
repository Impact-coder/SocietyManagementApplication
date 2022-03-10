package com.dhairya.societymanagementapplication.authActivity.authfragments.ui.createProfile

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

class createProfileViewModel constructor(
    private val state: SavedStateHandle // it's store all data of fragment in a bundle when app goes in onpause state
) : ViewModel() {

    //Live data events

    private val createProfileEventChannel = Channel<CreaterProfileEvent>()
    val createProfileEvent = createProfileEventChannel.receiveAsFlow()


    private val auth = FirebaseAuth.getInstance()

    //fetch data from fregement
    var email = state.get<String>("email") ?: ""
        set(value) {
            field = value
            state.set("email", value)
        }

    var password = state.get<String>("password") ?: ""
        set(value) {
            field = value
            state.set("password", value)
        }

    //fun for login
    fun login() {
        if (password.isBlank() || email.isBlank()) {
            val error = "The field must not be empty"
            showErrorMessage(error)
            return
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    auth.signInWithEmailAndPassword(email, password).await()
                    createProfileEventChannel.send(CreaterProfileEvent.NavigateBackWithResult(AUTH_RESULT_OK))
                } catch (e: Exception) {
                    showErrorMessage(e.message.toString())
                }
            }
        }


    }




    private fun showErrorMessage(text: String) = viewModelScope.launch {
        createProfileEventChannel.send(CreaterProfileEvent.ShowErrorMessage(text))
    }

    sealed class CreaterProfileEvent {
        data class ShowErrorMessage(val msg: String) : CreaterProfileEvent()
        data class NavigateBackWithResult(val result: Int) : CreaterProfileEvent()
    }

}