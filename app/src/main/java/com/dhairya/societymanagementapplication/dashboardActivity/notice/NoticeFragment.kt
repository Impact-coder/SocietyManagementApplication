package com.dhairya.societymanagementapplication.dashboardActivity.notice

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.databinding.FragmentNoticeBinding
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TOPIC="/topics/myTopic"

class NoticeFragment : Fragment(R.layout.fragment_notice) {

    private lateinit var binding: FragmentNoticeBinding
    val TAG="NoticeFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentNoticeBinding.bind(view)
        binding.apply {

            btnSend.setOnClickListener {
                val title=noticeTitle.text.toString()
                val message=noticeMessage.text.toString()
                if(title.isNotEmpty() && message.isNotEmpty()){
                    PushNotification(
                        NotificationData(title, message),
                        TOPIC
                    ).also {
                        sendNotification(it)
                    }
                }
            }

        }


    }
    private fun sendNotification(notification: PushNotification)= CoroutineScope(Dispatchers.IO).launch {
        try{
            val response=RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful)
            {
                Log.d(TAG,"response:${Gson().toJson(response)}")
            }else{
                Log.e(TAG,response.errorBody().toString())
            }
        }catch (e:Exception){
            Log.e(TAG,e.toString())
        }
    }

}