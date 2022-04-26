package com.dhairya.societymanagementapplication.authActivity.authfragments.ui.changePassword

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhairya.societymanagementapplication.authActivity.AUTH_RESULT_OK
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class changePasswordViewModel(
    private val state: SavedStateHandle
) : ViewModel() {

    //Live data events

    private val changePasswordEventChannel = Channel<ChangePasswordEvent>()
    val changePasswordEvent = changePasswordEventChannel.receiveAsFlow()

    private val resident = FirebaseFirestore.getInstance().collection("residents")

    var oldPassword = state.get<String>("oldPassword") ?: ""
        set(value) {
            field = value
            state.set("oldPassword", value)
        }

    var newPassword = state.get<String>("newPassword") ?: ""
        set(value) {
            field = value
            state.set("newPassword", value)
        }

    var confirmPassword = state.get<String>("confrimPassword") ?: ""
        set(value) {
            field = value
            state.set("confrimPassword", value)
        }


    fun changePassword() {
        val user = Firebase.auth.currentUser

        val credential = EmailAuthProvider
            .getCredential(user?.email!!, oldPassword)

        if (oldPassword.isEmpty())
        {
            val error = "Old Password can't be empty!!"
            showErrorMessage(error)
            return
        }
        else if (newPassword.isEmpty())
        {
            val error = "New Password can't be empty!!"
            showErrorMessage(error)
            return
        }
        else if (confirmPassword.isEmpty())
        {
            val error = "Confirm Password can't be empty!!"
            showErrorMessage(error)
            return

        }
        else
        {
            if (newPassword == confirmPassword)
            {
                viewModelScope.launch(Dispatchers.IO) {


                    user.reauthenticate(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            user.updatePassword(newPassword)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        //Redirect to deshboard
                                        CoroutineScope(Dispatchers.IO).launch {
                                            resident.document(user.uid).update("password",newPassword).await()
                                            changePasswordEventChannel.send(
                                                ChangePasswordEvent.NavigateBackWithResult(AUTH_RESULT_OK))
                                        }

                                    } else {
                                        showErrorMessage(task.exception.toString())
                                    }
                                }

                        } else {
                            showErrorMessage(it.exception.toString())
                        }
                    }
                }

            }
            else
            {
                val error = "New password and Confirm password must be same!!"
                showErrorMessage(error)
                return
            }
        }


    }

    private fun showErrorMessage(text: String) = viewModelScope.launch {
        changePasswordEventChannel.send(ChangePasswordEvent.ShowErrorMessage(text))
    }


    sealed class ChangePasswordEvent {
        data class ShowErrorMessage(val msg: String) : ChangePasswordEvent()
        data class NavigateBackWithResult(val result: Int) : ChangePasswordEvent()
    }


}