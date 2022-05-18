package com.dhairya.societymanagementapplication.dashboardActivity.addRule

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhairya.societymanagementapplication.dashboardActivity.rules.rulesViewModel
import com.dhairya.societymanagementapplication.data.ruleData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class addRuleViewModel (private val state: SavedStateHandle
) : ViewModel() {

    private val AddRuleEventChannel = Channel<addRuleViewModel.AddRuleEvent>()
    val addRuleEvent = AddRuleEventChannel.receiveAsFlow()

    val rule_data = FirebaseFirestore.getInstance().collection("ruleData")

    var textRule = state.get<String>("textRule") ?: ""
        set(value) {

            field = value
            state.set("textRule", value)
        }

    fun addRule() {

        try {

            if (textRule.isEmpty())
            {
                var error = "Rule can not be empty!!"
                showErrorMessage(error)
                return
            }
            else

            {
                CoroutineScope(Dispatchers.Main).launch {

                    val rid = rule_data.document().id

                    val ruleData = ruleData(
                        ruleid = rid,
                        rule = textRule.toString()

                    )

                    rule_data.document(rid).set(ruleData).await()
                    AddRuleEventChannel.send(
                        addRuleViewModel.AddRuleEvent.NavigateBackWithResult(
                            com.dhairya.societymanagementapplication.dashboardActivity.AUTH_RESULT_OK
                        )
                    )


                }
            }

        }
        catch (e:Exception)
        {
            showErrorMessage(e.message.toString())
        }

    }

    private fun showErrorMessage(text: String) = viewModelScope.launch {
        AddRuleEventChannel.send(addRuleViewModel.AddRuleEvent.ShowErrorMessage(text))
    }

    sealed class AddRuleEvent {
        data class ShowErrorMessage(val msg: String) : AddRuleEvent()
        data class NavigateBackWithResult(val result: Int) : AddRuleEvent()
    }


}