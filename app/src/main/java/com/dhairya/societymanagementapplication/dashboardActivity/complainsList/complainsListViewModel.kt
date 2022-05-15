package com.dhairya.societymanagementapplication.dashboardActivity.complainsList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhairya.societymanagementapplication.dashboardActivity.AUTH_RESULT_OK
import com.dhairya.societymanagementapplication.dashboardActivity.adapter.complainListAdapter
import com.dhairya.societymanagementapplication.dashboardActivity.fieComplain.fileComplainViewModel
import com.dhairya.societymanagementapplication.data.complainData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class complainsListViewModel(
    private val state: SavedStateHandle
) : ViewModel() {
    private val ComplainsListEventChannel = Channel<complainsListViewModel.ComplainsListEvent>()
    val complainsListEvent = ComplainsListEventChannel.receiveAsFlow()
    private val complain_data =
        FirebaseFirestore.getInstance().collection("complainData")

    fun getUpdatedList() {
        try {
            CoroutineScope(Dispatchers.Main).launch {
                 val list = complain_data.orderBy("complainDate").get().await()
                    .toObjects(complainData::class.java)
                ComplainsListEventChannel.send(
                    complainsListViewModel.ComplainsListEvent.NavigateBackWithResult(
                        AUTH_RESULT_OK, list
                    )
                )
            }


        } catch (e: Exception) {
            showErrorMessage(e.message.toString())
        }


    }

    private fun showErrorMessage(text: String) = viewModelScope.launch {
        ComplainsListEventChannel.send(ComplainsListEvent.ShowErrorMessage(text))
    }

    sealed class ComplainsListEvent {
        data class ShowErrorMessage(val msg: String) : ComplainsListEvent()
        data class NavigateBackWithResult(val result: Int, val list: List<complainData>) :
            ComplainsListEvent()
    }

}