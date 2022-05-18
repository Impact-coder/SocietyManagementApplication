package com.dhairya.societymanagementapplication.dashboardActivity.notice

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.authActivity.authfragments.ui.login.exhaustive
import com.dhairya.societymanagementapplication.dashboardActivity.addTransaction.addTransactionViewModel
import com.dhairya.societymanagementapplication.dashboardActivity.fieComplain.fileComplainFragmentDirections
import com.dhairya.societymanagementapplication.data.NotificationData
import com.dhairya.societymanagementapplication.databinding.FragmentNoticeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TOPIC="/topics/myTopic"

class NoticeFragment : Fragment(R.layout.fragment_notice) {

    private val viewModel: noticeViewModel by viewModels()
    private lateinit var binding: FragmentNoticeBinding
    val TAG="NoticeFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentNoticeBinding.bind(view)
        binding.apply {

            noticeTitle.setText(viewModel.title)
            noticeMessage.setText(viewModel.message)

            noticeMessage.addTextChangedListener {
                viewModel.message = it.toString()
            }

            noticeTitle.addTextChangedListener {
                viewModel.title = it.toString()
            }

            btnSend.setOnClickListener {
                val title=noticeTitle.text.toString()
                val message=noticeMessage.text.toString()

                viewModel.addNotice()

                if(title.isNotEmpty() && message.isNotEmpty()){
                    PushNotification(
                        NotificationData(title, message),
                        TOPIC
                    ).also {

                        sendNotification(it)
                    }
                    }
                }
            btnBack.setOnClickListener {
                findNavController().navigate(NoticeFragmentDirections.actionNoticeFragmentToDashBoardFragment())
            }
            }



        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.noticeEvent.collect { events ->
                when (events) {

                    is noticeViewModel.NoticeEvent.NavigateBackWithResult ->  {
                        Toast.makeText(
                            context,
                            "Notice Send Successfully!!",
                            Toast.LENGTH_SHORT
                        ).show()

                        binding.noticeTitle.setText("")
                        binding.noticeMessage.setText("")
                    }
                    is noticeViewModel.NoticeEvent.ShowErrorMessage -> {
                        Toast.makeText(requireContext(), events.msg, Toast.LENGTH_SHORT).show()


                    }
                }.exhaustive
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