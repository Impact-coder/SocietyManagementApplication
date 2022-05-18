package com.dhairya.societymanagementapplication.dashboardActivity.rules

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhairya.societymanagementapplication.data.ruleData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class rulesViewModel (private val state: SavedStateHandle
) : ViewModel() {

    private val RulesEventChannel = Channel<rulesViewModel.AddRulesEvent>()
    val rulesEvent = RulesEventChannel.receiveAsFlow()

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
                    RulesEventChannel.send(
                        rulesViewModel.AddRulesEvent.NavigateBackWithResult(
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
        RulesEventChannel.send(rulesViewModel.AddRulesEvent.ShowErrorMessage(text))
    }

    sealed class AddRulesEvent {
        data class ShowErrorMessage(val msg: String) : AddRulesEvent()
        data class NavigateBackWithResult(val result: Int) : AddRulesEvent()
    }


}