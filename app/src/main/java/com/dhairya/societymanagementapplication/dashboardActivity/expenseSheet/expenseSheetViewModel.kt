package com.dhairya.societymanagementapplication.dashboardActivity.expenseSheet

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhairya.societymanagementapplication.authActivity.authfragments.ui.forgotpassword.forgotPasswordViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class expenseSheetViewModel(
    private val state: SavedStateHandle
):ViewModel() {

    private val expenseSheetEventChannel = Channel<expenseSheetViewModel.ExpenseSheetEvent>()
    val expenseSheetEvent = expenseSheetEventChannel.receiveAsFlow()

    var startDate  = state.get<String>("startDate") ?: ""
        set(value) {
            field = value
            state.set("startDate", value)
        }

    var endDate = state.get<String>("endDate") ?: ""
        set(value) {
            field = value
            state.set("endDate", value)
        }



    private fun showErrorMessage(text: String) = viewModelScope.launch {
        expenseSheetEventChannel.send(ExpenseSheetEvent.ShowErrorMessage(text))
    }



    sealed class ExpenseSheetEvent {
        data class ShowErrorMessage(val msg: String) : ExpenseSheetEvent()
        data class NavigateBackWithResult(val result: Int) : ExpenseSheetEvent()
    }

}