package com.dhairya.societymanagementapplication.dashboardActivity.residentList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.dhairya.societymanagementapplication.dashboardActivity.adapter.residantAdapter
import com.dhairya.societymanagementapplication.data.profileData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class residentListViewModel(
    private val state: SavedStateHandle
) : ViewModel() {

    private lateinit var residentProfileArrayList: ArrayList<profileData>
    private lateinit var residentAdapter:residantAdapter
    private lateinit var recycleView: RecyclerView

    private val profile_data = FirebaseFirestore.getInstance().collection("profileData")

    fun showResidentList() {

    }
}