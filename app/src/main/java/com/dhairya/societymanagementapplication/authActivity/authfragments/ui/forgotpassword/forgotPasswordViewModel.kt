package com.dhairya.societymanagementapplication.authActivity.authfragments.ui.forgotpassword

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

class forgotPasswordViewModel constructor(
    private val state: SavedStateHandle
): ViewModel() {

    private val forgotPasswordEventChannel = Channel<ForgotPasswordEvent>()
    val forgotPasswordEvent = forgotPasswordEventChannel.receiveAsFlow()
    private val auth = FirebaseAuth.getInstance()

    var email = state.get<String>("email") ?: ""
        set(value) {
            field = value
            state.set("email", value)
        }

    fun forgotPassword() {
        if (email.isBlank()) {
            val error = "The field must not be empty!!"
            showErrorMessage(error)
            return
        } else {
            viewModelScope.launch(Dispatchers.Main) {
                try {
                    auth.sendPasswordResetEmail(email).await()
                } catch (e: Exception) {
                    showErrorMessage(e.message.toString())
                }
                forgotPasswordEventChannel.send(
                    ForgotPasswordEvent.NavigateBackWithResult(
                        AUTH_RESULT_OK
                    )
                )
            }
        }
    }

    private fun showErrorMessage(text: String) = viewModelScope.launch {
        forgotPasswordEventChannel.send(ForgotPasswordEvent.ShowErrorMessage(text))
    }

    sealed class ForgotPasswordEvent {
        data class ShowErrorMessage(val msg: String) : ForgotPasswordEvent()
        data class NavigateBackWithResult(val result: Int) : ForgotPasswordEvent()
    }

}