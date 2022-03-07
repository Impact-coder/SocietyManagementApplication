package com.dhairya.societymanagementapplication.dashboardActivity.addMember

import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhairya.societymanagementapplication.authActivity.authfragments.ui.changePassword.changePasswordViewModel
import com.dhairya.societymanagementapplication.dashboardActivity.AUTH_RESULT_OK
import com.dhairya.societymanagementapplication.residentsData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class addMemberViewModel (
    private val state: SavedStateHandle
) : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    private val residents = FirebaseFirestore.getInstance().collection("residents")

    //Live data events

    private val AddMemberEventChannel = Channel<AddMemberEvent>()
    val addMemberEvent = AddMemberEventChannel.receiveAsFlow()
    var member_password: String = "SMA@cp2"



    var addMemberEmailEdittext= state.get<String>("addMemberEmailEdittext") ?: ""
        set(value) {
            field = value
            state.set("addMemberEmailEdittext", value)
        }

    var addMemberFlatnoEdittext = state.get<String>("addMemberFlatnoEdittext") ?: ""
        set(value) {
            field = value
            state.set("addMemberFlatnoEdittext", value)
        }



    fun addMember(){

        if (addMemberEmailEdittext.isBlank() || addMemberFlatnoEdittext.isBlank()) {
            val error = "The field must not be empty"
            showErrorMessage(error)
            return
        }else {

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    auth.createUserWithEmailAndPassword(
                        addMemberEmailEdittext,
                        member_password
                    ).await()

                } catch (e: Exception) {
                    showErrorMessage(e.message.toString())
                }

                if (auth.currentUser != null) {
                    val uid = auth.currentUser!!.uid
                    val resident = residentsData(
                        uid = uid,
                        email = addMemberEmailEdittext,
                        flatNo = addMemberFlatnoEdittext,

                    )
                    residents.document(uid).set(resident).await()
                    AddMemberEventChannel.send(AddMemberEvent.NavigateBackWithResult(AUTH_RESULT_OK))

                }

            }
        }

        
        

    }

    private fun showErrorMessage(text: String) = viewModelScope.launch {
        AddMemberEventChannel.send(AddMemberEvent.ShowErrorMessage(text))
    }

    sealed class AddMemberEvent {
        data class ShowErrorMessage(val msg: String) : AddMemberEvent()
        data class NavigateBackWithResult(val result: Int) : AddMemberEvent()
    }


}