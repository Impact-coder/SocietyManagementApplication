package com.dhairya.societymanagementapplication.dashboardActivity.noticeList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.dashboardActivity.adapter.noticeAdapter
import com.dhairya.societymanagementapplication.data.noticeData
import com.dhairya.societymanagementapplication.databinding.FragmentNoticeListBinding
import com.google.firebase.firestore.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class noticeListFragment : Fragment(R.layout.fragment_notice_list) {

    private val viewModel: noticeListViewModel by viewModels()
    private lateinit var binding: FragmentNoticeListBinding

    private lateinit var noticeArrayList: ArrayList<noticeData>
    private lateinit var noticesDisplayAdapter: noticeAdapter
    private lateinit var recycleView: RecyclerView

    private val notice_data = FirebaseFirestore.getInstance().collection("noticeData")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding = FragmentNoticeListBinding.bind(view)

        binding.apply {


            recycleView = binding.noticeRecyclerview
            noticeArrayList = arrayListOf()
            recycleView.layoutManager = LinearLayoutManager(context)

            CoroutineScope(Dispatchers.Main).launch {

                val list = notice_data.orderBy("dateTime", Query.Direction.ASCENDING).get().await()
                    .toObjects(noticeData::class.java)


                noticesDisplayAdapter = noticeAdapter(
                    requireContext(),
                    list.toList()
                )
                recycleView.adapter = noticesDisplayAdapter

            }

//            EventChangeListener()


        }

    }

//    private fun EventChangeListener() {
//        notice_data.addSnapshotListener(object : EventListener<QuerySnapshot> {
//            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
//                if (error != null){
//                    Log.e("FireStore error", error.message.toString())
//                    return
//                }
//                for (dc: DocumentChange in value?.documentChanges!!)
//                {
//                    if(dc.type == DocumentChange.Type.ADDED){
//
//                        noticeArrayList.add(dc.document.toObject(noticeData::class.java))
//                    }
//                }
//                noticeAdapter.notifyDataSetChanged()
//            }
//
//        })
//    }


}