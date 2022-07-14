package com.dhairya.societymanagementapplication.dashboardActivity.complainsList

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.authActivity.authfragments.ui.login.exhaustive
import com.dhairya.societymanagementapplication.dashboardActivity.adapter.complainListAdapter
import com.dhairya.societymanagementapplication.dashboardActivity.fieComplain.fileComplainViewModel
import com.dhairya.societymanagementapplication.dashboardActivity.residentList.residentListFragmentDirections
import com.dhairya.societymanagementapplication.data.complainData
import com.dhairya.societymanagementapplication.databinding.FragmentComplainsListBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class complainsListFragment : Fragment(R.layout.fragment_complains_list) {

    private val viewModel: complainsListViewModel by viewModels()

    private lateinit var binding: FragmentComplainsListBinding

    private lateinit var complainArrayList: ArrayList<complainData>
    private lateinit var complainDisplayListAdapter: complainListAdapter
    private lateinit var recycleView: RecyclerView
    private lateinit var complainList: List<complainData>
    private val complain_data =
        FirebaseFirestore.getInstance().collection("complainData")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        CoroutineScope(Dispatchers.Main).launch {
//            viewModel.getUpdatedList()
//        }


        binding = FragmentComplainsListBinding.bind(view)

        binding.apply {

            recycleView = binding.complainRecycleView
            complainArrayList = arrayListOf()
            recycleView.layoutManager = LinearLayoutManager(context)


            CoroutineScope(Dispatchers.Main).launch {
                val list = complain_data.orderBy("complainDate",Query.Direction.ASCENDING).get().await()
                    .toObjects(complainData::class.java)
                complainDisplayListAdapter = complainListAdapter(requireContext(), list.toList())
                recycleView.adapter = complainDisplayListAdapter

                complainDisplayListAdapter.setOnItemClickListner { complainData ->
                    Log.d("ComplainsListFragment: ", complainData.toString())
                    val action =
                        complainsListFragmentDirections.actionComplainsListFragmentToComplainFragment(
                            complainData
                        )
                    findNavController().navigate(action)
                }
            }

//            EventChangeListener()


            btnBack.setOnClickListener {
                findNavController().navigate(complainsListFragmentDirections.actionComplainsListFragmentToDashBoardFragment())
            }

        }
//        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            viewModel.complainsListEvent.collect { events ->
//                when (events) {
//
//                    is complainsListViewModel.ComplainsListEvent.NavigateBackWithResult -> {
////                        Toast.makeText(
////                            context,
////                            "Complain Filed Successfully!!",
////                            Toast.LENGTH_SHORT
////                        ).show()
//                            complainList = events.list
//
//
//                    }
//                    is complainsListViewModel.ComplainsListEvent.ShowErrorMessage -> {
//                        Snackbar.make(requireView(), events.msg, Snackbar.LENGTH_LONG).show()
//
//                    }
//                }.exhaustive
//            }
//
//        }
    }

//    private fun EventChangeListener() {
//
//        complain_data.addSnapshotListener(object : EventListener<QuerySnapshot> {
//            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
//                if (error != null) {
//                    Log.e("FireStore error", error.message.toString())
//                    return
//                }
//
//                for (dc: DocumentChange in value?.documentChanges!!) {
//                    if (dc.type == DocumentChange.Type.ADDED) {
//
//                        Log.d("TAG_COMPLAIN_LIST", "onEvent: ${dc.document}")
//
//                        complainArrayList.add(dc.document.toObject(complainData::class.java))
//
//                    }
//                }
//                complainDisplayListAdapter.notifyDataSetChanged()
//            }
//
//        })
//    }




}