package com.dhairya.societymanagementapplication.dashboardActivity.fieComplain

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhairya.societymanagementapplication.dashboardActivity.AUTH_RESULT_OK
import com.dhairya.societymanagementapplication.dashboardActivity.notice.noticeViewModel
import com.dhairya.societymanagementapplication.data.complainData
import com.dhairya.societymanagementapplication.data.profileData
import com.dhairya.societymanagementapplication.data.residentsData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class fileComplainViewModel(
    private val state: SavedStateHandle
) : ViewModel() {
    private val FileComplainEventChannel = Channel<fileComplainViewModel.FileComplainEvent>()
    val fileComplainEvent = FileComplainEventChannel.receiveAsFlow()

    private val resident_data = FirebaseFirestore.getInstance().collection("residents")
    private val complain_data = FirebaseFirestore.getInstance().collection("complainData")
    private val profile_data = FirebaseFirestore.getInstance().collection("profileData")

    private var profile: MutableList<profileData> = mutableListOf()
    private var residents: MutableList<residentsData> = mutableListOf()
    lateinit var flatNo: String
    lateinit var userName: String


    var complainMessage = state.get<String>("complainMessage") ?: ""
        set(value) {
            field = value
            state.set("complainMessage", value)
        }

    var complainSubject = state.get<String>("complainSubject") ?: ""
        set(value) {
            field = value
            state.set("complainSubject", value)
        }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fileComplain() {

        if (complainSubject.isEmpty()) {
            val error = "Subject can not be empty!!"
            showErrorMessage(error)
            return
        } else if (complainMessage.isEmpty()) {
            val error = "Complain can not be empty!!"
            showErrorMessage(error)
            return
        } else {
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val formattedDate = currentDateTime.format(formatter)

            CoroutineScope(Dispatchers.IO).launch {


                residents = resident_data.whereEqualTo("uid", Firebase.auth.currentUser!!.uid).get()
                    .await().toObjects(residentsData::class.java)

                profile =
                    profile_data.whereEqualTo("memberid", Firebase.auth.currentUser!!.uid).get()
                        .await().toObjects(profileData::class.java)

                flatNo = residents[0].flatNo
                userName = profile[0].fullName

                try {

                    val cId = complain_data.document().id
                    val complain = complainData(
                        complainDate = formattedDate,
                        complainSubject = complainSubject,
                        complain = complainMessage,
                        complainResponse = "",
                        cid = cId,
                        memberId = Firebase.auth.currentUser!!.uid,
                        flatNO = flatNo,
                        memberName = userName

                    )

                    complain_data.document(cId).set(complain).await()
                    // transactionData.orderBy(date,Query.Direction.ASCENDING)
                    FileComplainEventChannel.send(
                        fileComplainViewModel.FileComplainEvent.NavigateBackWithResult(
                            AUTH_RESULT_OK/*, listOf()*/
                        )
                    )

                } catch (e: Exception) {
                    showErrorMessage(e.message.toString())
                }


            }

        }
    }

//    private fun getUserdata() {
//
//        try {
//            CoroutineScope(Dispatchers.IO).launch {
//                residents = resident_data.whereEqualTo("uid", Firebase.auth.currentUser!!.uid).get()
//                    .await().toObjects(residentsData::class.java)
//
//                profile =
//                    profile_data.whereEqualTo("memberid", Firebase.auth.currentUser!!.uid).get()
//                        .await().toObjects(profileData::class.java)
//
//                flatNo = residents[0].flatNo
//                userName = profile[0].fullName
//
//
//            }
//        } catch (e: Exception) {
//            showErrorMessage(e.message.toString())
//        }
//    }

    private fun showErrorMessage(text: String) = viewModelScope.launch {
        FileComplainEventChannel.send(FileComplainEvent.ShowErrorMessage(text))
    }

    sealed class FileComplainEvent {
        data class ShowErrorMessage(val msg: String) : FileComplainEvent()
        data class NavigateBackWithResult(val result: Int,/* val list: List<complainData>*/) : FileComplainEvent()
    }
}