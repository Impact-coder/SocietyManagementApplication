package com.dhairya.societymanagementapplication.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class complainData(
    val complainDate: String= "",
    val flatNO:String="",
    val memberName:String="",
    val complainSubject: String="",
    val complain: String="",
    val memberId: String="",
    val complainResponse: String="",
    val cid: String=""

):Parcelable