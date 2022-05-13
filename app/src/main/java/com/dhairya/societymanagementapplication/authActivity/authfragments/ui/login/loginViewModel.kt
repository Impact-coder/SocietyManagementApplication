package com.dhairya.societymanagementapplication.authActivity.authfragments.ui.login

import android.content.SharedPreferences
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

class loginViewModel constructor(
    private val state: SavedStateHandle // it's store all data of fragment in a bundle when app goes in onpause state
) : ViewModel() {

    //Live data events
    private val loginEventChannel = Channel<LoginEvent>()
    val loginEvent = loginEventChannel.receiveAsFlow()


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
                    loginEventChannel.send(LoginEvent.NavigateBackWithResult(AUTH_RESULT_OK))
                } catch (e: Exception) {

                    showErrorMessage(e.message.toString())
                }
            }
        }


    }




    private fun showErrorMessage(text: String) = viewModelScope.launch {
        loginEventChannel.send(LoginEvent.ShowErrorMessage(text))
    }

    sealed class LoginEvent {
        data class ShowErrorMessage(val msg: String) : LoginEvent()
        data class NavigateBackWithResult(val result: Int) : LoginEvent()
    }

}