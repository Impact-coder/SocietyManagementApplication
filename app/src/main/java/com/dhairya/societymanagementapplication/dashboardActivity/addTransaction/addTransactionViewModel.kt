package com.dhairya.societymanagementapplication.dashboardActivity.addTransaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhairya.societymanagementapplication.dashboardActivity.AUTH_RESULT_OK
import com.dhairya.societymanagementapplication.dashboardActivity.addMember.addMemberViewModel
import com.dhairya.societymanagementapplication.data.transactionData
import com.dhairya.societymanagementapplication.databinding.FragmentAddTransactionBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.*

class addTransactionViewModel(private val state: SavedStateHandle) : ViewModel() {

    private val AddTransactionEventChannel = Channel<AddTransactionEvent>()
    val addTransactionEvent = AddTransactionEventChannel.receiveAsFlow()

    var cal = Calendar.getInstance()
    var date = ""

    private val transactionData = FirebaseFirestore.getInstance().collection("transactionData")


    var paricularasEditText = state.get<String>("paricularasEditText") ?: ""
        set(value) {
            field = value
            state.set("paricularasEditText", value)
        }

    var amountEditText = state.get<String>("amountEditText") ?: ""
        set(value) {
            field = value
            state.set("amountEdittext", value)
        }

    var btnSelectDate = state.get<String>("btnSelectDate") ?: ""
        set(value) {
            field = value
            state.set("btnSelectDate", value)
        }

    private fun showErrorMessage(text: String) = viewModelScope.launch {
        AddTransactionEventChannel.send(AddTransactionEvent.ShowErrorMessage(text))
    }

    fun addTransaction() {

        if (btnSelectDate.isEmpty()) {
            val error = "Date can not be empty!!"
            showErrorMessage(error)
            return
        } else if (paricularasEditText.isEmpty()) {
            val error = "Particular can not be empty!!"
            showErrorMessage(error)
            return
        } else if (amountEditText.isEmpty()) {
            val error = "Amount can not be empty!!"
            showErrorMessage(error)
            return
        } else {

            viewModelScope.launch(Dispatchers.IO){
                try {
                    val transaction_id = transactionData.document().id
                    val transaction = transactionData(
                        tid = transaction_id,
                       date = btnSelectDate.toString(),
                        particular = paricularasEditText,
                        amount = amountEditText

                    )

                    transactionData.document(transaction_id).set(transaction).await()
                   // transactionData.orderBy(date,Query.Direction.ASCENDING)
                    AddTransactionEventChannel.send(
                       AddTransactionEvent.NavigateBackWithResult(
                            AUTH_RESULT_OK
                        ))

                } catch (e: Exception) {
                    showErrorMessage(e.message.toString())
                }
            }
        }
    }


    sealed class AddTransactionEvent {
        data class ShowErrorMessage(val msg: String) : AddTransactionEvent()
        data class NavigateBackWithResult(val result: Int) : AddTransactionEvent()
    }
}