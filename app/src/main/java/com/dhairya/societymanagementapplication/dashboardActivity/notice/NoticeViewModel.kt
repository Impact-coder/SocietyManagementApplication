package com.dhairya.societymanagementapplication.dashboardActivity.notice

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhairya.societymanagementapplication.dashboardActivity.AUTH_RESULT_OK
import com.dhairya.societymanagementapplication.dashboardActivity.addTransaction.addTransactionViewModel
import com.dhairya.societymanagementapplication.data.NotificationData
import com.dhairya.societymanagementapplication.data.noticeData
import com.dhairya.societymanagementapplication.data.transactionData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class noticeViewModel(
    private val state: SavedStateHandle
): ViewModel() {

    private val noticeEventChannel = Channel<NoticeEvent>()
    val noticeEvent = noticeEventChannel.receiveAsFlow()
    private var notice_data = FirebaseFirestore.getInstance().collection("noticeData")

    val TAG="NoticeFragment"


    var title   = state.get<String>("title") ?: ""
        set(value) {
            field = value
            state.set("title", value)
        }

    var message = state.get<String>("message") ?: ""
        set(value) {
            field = value
            state.set("message", value)
        }



    private fun showErrorMessage(text: String) = viewModelScope.launch {
        noticeEventChannel.send(NoticeEvent.ShowErrorMessage(text))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addNotice() {
        if (title.isEmpty())
        {
            var error = "Title can not be empty!!"
            showErrorMessage(error)
            return
        }
        else if(message.isEmpty())
        {
            var error = "Message can not be empty!!"
            return
        }
        else{

            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val formattedDateTime = currentDateTime.format(formatter)
            CoroutineScope(Dispatchers.IO).launch {

                try {


                    val nId = notice_data.document().id
                    val notice = noticeData(
                        noticeId = nId,
                        title = title,
                        message = message,
                        dateTime = formattedDateTime

                    )

                    notice_data.document(nId).set(notice).await()
                    // transactionData.orderBy(date,Query.Direction.ASCENDING)
                    noticeEventChannel.send(
                        noticeViewModel.NoticeEvent.NavigateBackWithResult(
                            AUTH_RESULT_OK
                        ))

                }
                catch (e:Exception)
                {
                    showErrorMessage(e.message.toString())
                }

            }
        }


    }

//    private fun sendNotification(notification: PushNotification)= CoroutineScope(Dispatchers.IO).launch {
//        try{
//            val response=RetrofitInstance.api.postNotification(notification)
//            if(response.isSuccessful)
//            {
//                Log.d(TAG,"response:${Gson().toJson(response)}")
//            }else{
//                Log.e(TAG,response.errorBody().toString())
//            }
//        }catch (e:Exception){
//            Log.e(TAG,e.toString())
//        }
//    }


    sealed class NoticeEvent {
        data class ShowErrorMessage(val msg: String) : NoticeEvent()
        data class NavigateBackWithResult(val result: Int) : NoticeEvent()
    }

}