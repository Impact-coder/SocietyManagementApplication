package com.dhairya.societymanagementapplication.dashboardActivity.complain

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhairya.societymanagementapplication.dashboardActivity.AUTH_RESULT_OK
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ComplainViewModel( private val state: SavedStateHandle
) : ViewModel() {
    private val ComplainEventChannel = Channel<ComplainViewModel.RulesEvent>()
    val complainEvent = ComplainEventChannel.receiveAsFlow()
    val complain_data = FirebaseFirestore.getInstance().collection("complainData")

    var complainreply = state.get<String>("complainreply") ?: ""
        set(value) {
            field = value
            state.set("complainreply", value)
        }

    var complainreponse = state.get<String>("complainreponse") ?: ""
        set(value) {
            field = value
            state.set("complainreponse", value)
        }

    private fun showErrorMessage(text: String) = viewModelScope.launch {
        ComplainEventChannel.send(RulesEvent.ShowErrorMessage(text))
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

                CoroutineScope(Dispatchers.IO).launch {
                    complain_data.document(complainId).update(
                            "complainResponse",complainreply
                    )



                    ComplainEventChannel.send(
                        ComplainViewModel.RulesEvent.NavigateBackWithResult(
                            AUTH_RESULT_OK
                        )
                    )
                }

            }
            catch (e:Exception)
            {
                showErrorMessage(e.message.toString())
            }
        }
    }

    sealed class RulesEvent {
        data class ShowErrorMessage(val msg: String) : RulesEvent()
        data class NavigateBackWithResult(val result: Int) : RulesEvent()
    }
}