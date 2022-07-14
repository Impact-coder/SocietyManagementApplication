package com.dhairya.societymanagementapplication.dashboardActivity.myComplains

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.dashboardActivity.adapter.complainListAdapter
import com.dhairya.societymanagementapplication.data.complainData
import com.dhairya.societymanagementapplication.databinding.FragmentMyComplainsBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class myComplainsFragment : Fragment(R.layout.fragment_my_complains) {


    private val viewModel: myComplainsViewModel by viewModels()

    private lateinit var binding: FragmentMyComplainsBinding

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


        binding = FragmentMyComplainsBinding.bind(view)

        binding.apply {

            recycleView = binding.myComplainRecycleView
            complainArrayList = arrayListOf()
            recycleView.layoutManager = LinearLayoutManager(context)




            CoroutineScope(Dispatchers.Main).launch {
                val list = complain_data.whereEqualTo("memberId", Firebase.auth.currentUser!!.uid)
                    .orderBy("complainDate", Query.Direction.ASCENDING).get().await()
                    .toObjects(complainData::class.java)
                complainDisplayListAdapter = complainListAdapter(requireContext(), list.toList())
                recycleView.adapter = complainDisplayListAdapter

                complainDisplayListAdapter.setOnItemClickListner { complainData ->
                    Log.d("ComplainsListFragment: ", complainData.toString())
                    val action =
                        myComplainsFragmentDirections.actionMyComplainsFragmentToMyComplainDisplayFragment(
                            complainData
                        )
                    findNavController().navigate(action)
                }
            }


            btnBack.setOnClickListener {
                findNavController().navigate(myComplainsFragmentDirections.actionMyComplainsFragmentToDashBoardFragment())
            }

        }


    }
}
