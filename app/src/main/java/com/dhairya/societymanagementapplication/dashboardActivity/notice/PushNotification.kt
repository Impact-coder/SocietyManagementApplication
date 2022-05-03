package com.dhairya.societymanagementapplication.dashboardActivity.notice

import com.dhairya.societymanagementapplication.data.NotificationData

data class PushNotification(
    val data: NotificationData,
    val to:String
)