package com.dhairya.societymanagementapplication.dashboardActivity.residentList

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.authActivity.authfragments.ui.login.loginViewModel
import com.dhairya.societymanagementapplication.dashboardActivity.adapter.residantAdapter
import com.dhairya.societymanagementapplication.data.profileData
import com.dhairya.societymanagementapplication.databinding.FragmentCreateProfileBinding
import com.dhairya.societymanagementapplication.databinding.FragmentLoginBinding
import com.dhairya.societymanagementapplication.databinding.FragmentResidentListBinding
import com.google.firebase.firestore.*
import javax.xml.parsers.DocumentBuilder


class residentListFragment : Fragment(R.layout.fragment_resident_list) {

    private val viewModel: residentListViewModel by viewModels()
    private lateinit var binding: FragmentResidentListBinding

    private lateinit var residentProfileArrayList: ArrayList<profileData>
    private lateinit var residentProfileAdapter: residantAdapter
    private lateinit var recycleView: RecyclerView

    private val profile_data = FirebaseFirestore.getInstance().collection("profileData")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding = FragmentResidentListBinding.bind(view)

        binding.apply {
            recycleView = binding.residentRecycleView
            residentProfileArrayList = arrayListOf()
            recycleView.layoutManager = LinearLayoutManager(context)
            residentProfileAdapter = residantAdapter(requireContext(),residentProfileArrayList)

            recycleView.adapter = residentProfileAdapter

            EventChangeListener()

            btnBack.setOnClickListener {
                findNavController().navigate(residentListFragmentDirections.actionResidentListFragmentToDashBoardFragment())
            }

        }

    }

    private fun EventChangeListener() {
        profile_data.addSnapshotListener(object :EventListener<QuerySnapshot>{
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null){
                        Log.e("FireStore error", error.message.toString())
                        return
                    }
                for (dc: DocumentChange in value?.documentChanges!!)
                {
                    if(dc.type == DocumentChange.Type.ADDED){

                        residentProfileArrayList.add(dc.document.toObject(profileData::class.java))
                    }
                }
                residentProfileAdapter.notifyDataSetChanged()
            }

        })
    }
}