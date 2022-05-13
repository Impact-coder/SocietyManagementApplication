package com.dhairya.societymanagementapplication.dashboardActivity.complainsList

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.dashboardActivity.adapter.complainListAdapter
import com.dhairya.societymanagementapplication.dashboardActivity.residentList.residentListFragmentDirections
import com.dhairya.societymanagementapplication.data.complainData
import com.dhairya.societymanagementapplication.databinding.FragmentComplainsListBinding
import com.google.firebase.firestore.*


class complainsListFragment : Fragment(R.layout.fragment_complains_list) {

    private lateinit var binding: FragmentComplainsListBinding

    private lateinit var complainArrayList: ArrayList<complainData>
    private lateinit var complainListAdapter: complainListAdapter
    private lateinit var recycleView: RecyclerView
    private val complain_data = FirebaseFirestore.getInstance().collection("complainData")

    fun onCreate(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentComplainsListBinding.bind(view)

        binding.apply {

            recycleView = binding.complainRecycleView
            complainArrayList = arrayListOf()
            recycleView.layoutManager = LinearLayoutManager(context)
            complainListAdapter = complainListAdapter(requireContext(),complainArrayList)

            recycleView.adapter = complainListAdapter

            EventChangeListener()

            btnBack.setOnClickListener {
                findNavController().navigate(residentListFragmentDirections.actionResidentListFragmentToDashBoardFragment())
            }

        }
    }

    private fun EventChangeListener() {
        complain_data.addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("FireStore error", error.message.toString())
                    return
                }

                for (dc: DocumentChange in value?.documentChanges!!)
                {
                    if(dc.type == DocumentChange.Type.ADDED){

                        complainArrayList.add(dc.document.toObject(complainData::class.java))

                    }
                }
                complainListAdapter.notifyDataSetChanged()
            }

        })
    }


}