package com.dhairya.societymanagementapplication.data

import com.google.android.gms.common.api.Response

data class complainData(
    val complainDate: String,
    val flatNO:String,
    val memberName:String,
    val complainSubject: String,
    val complain: String,
    val memberId: String,
    val complainResponse: String,
    val cid: String

)