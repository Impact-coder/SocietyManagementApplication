package com.dhairya.societymanagementapplication.dashboardActivity.complain

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhairya.societymanagementapplication.dashboardActivity.AUTH_RESULT_OK
import com.dhairya.societymanagementapplication.dashboardActivity.editProfile.editProfileViewModel
import com.dhairya.societymanagementapplication.dashboardActivity.fieComplain.fileComplainViewModel
import com.dhairya.societymanagementapplication.data.complainData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ComplainViewModel( private val state: SavedStateHandle
) : ViewModel() {
    private val ComplainEventChannel = Channel<ComplainViewModel.ComplainEvent>()
    val complainEvent = ComplainEventChannel.receiveAsFlow()
    val complain_data = FirebaseFirestore.getInstance().collection("complainData")

    var complainreply = state.get<String>("complainreply") ?: ""
        set(value) {
            field = value
            state.set("complainreply", value)
        }

    private fun showErrorMessage(text: String) = viewModelScope.launch {
        ComplainEventChannel.send(ComplainEvent.ShowErrorMessage(text))
    }

    fun setComplainReply(complainId:String) {

        if (complainreply.isEmpty())
        {
            val error = "Reply can not be empty!!"
            showErrorMessage(error)
            return
        }
        else
        {
            try {

//                CoroutineScope(Dispatchers.IO).launch {
//                    complain_data.document(complainId).update(
//
//                    )
//
//                    ComplainEventChannel.send(
//                        ComplainViewModel.ComplainEvent.NavigateBackWithResult(
//                            AUTH_RESULT_OK
//                        )
//                }


            }
            catch (e:Exception)
            {
                showErrorMessage(e.message.toString())
            }
        }
    }

    sealed class ComplainEvent {
        data class ShowErrorMessage(val msg: String) : ComplainEvent()
        data class NavigateBackWithResult(val result: Int) : ComplainEvent()
    }
}