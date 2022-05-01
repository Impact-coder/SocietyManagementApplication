package com.dhairya.societymanagementapplication.dashboardActivity.expenseSheet

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.dashboardActivity.adapter.TableRowAdapter
import com.dhairya.societymanagementapplication.data.transactionData
import com.dhairya.societymanagementapplication.databinding.FragmentExpenseSheetBinding
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*


class expenseSheetFragment : Fragment(R.layout.fragment_expense_sheet) {

    private val viewModel: expenseSheetViewModel by viewModels()
    private lateinit var binding: FragmentExpenseSheetBinding
    private var expense_data = FirebaseFirestore.getInstance().collection("transactionData")
    private var expenseDataArrayList: MutableList<transactionData> = mutableListOf()
    private lateinit var tableRowAdapter: TableRowAdapter


    var sDate = ""
    var eDate = ""
    val myFormat = "dd/MM/yyyy" // mention the format you need
    val sdf = SimpleDateFormat(myFormat, Locale.US)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        var cal = Calendar.getInstance()

        binding = FragmentExpenseSheetBinding.bind(view)
        binding.apply {

            startDate.setText(viewModel.startDate)
            endDate.setText(viewModel.endDate)

            startDate.addTextChangedListener {
                viewModel.startDate = it.toString()
            }

            endDate.addTextChangedListener {
                viewModel.endDate = it.toString()
            }

            val dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)


                    startDate.textSize = 17F
                    sDate = sdf.format(cal.getTime())
                    startDate.text = sdf.format(cal.getTime())
                }

            val dateSetListener1 =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    endDate.textSize = 17F
                    eDate = sdf.format(cal.getTime())
                    endDate.text = sdf.format(cal.getTime())
                }



            startDate.setOnClickListener {
                DatePickerDialog(
                    requireContext(),
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            endDate.setOnClickListener {
                DatePickerDialog(
                    requireContext(),
                    dateSetListener1,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            btnView.setOnClickListener {

                if (startDate.text.isEmpty()) {
                    Toast.makeText(context, "Please enter starting date!!", Toast.LENGTH_SHORT)
                        .show()
                } else if (endDate.text.isEmpty()) {
                    Toast.makeText(context, "Please enter ending date!!", Toast.LENGTH_SHORT).show()
                } else {

                    var startingDate: Date = sdf.parse(sDate)
                    var endingDate: Date = sdf.parse(eDate)

                    if (startingDate.before(endingDate) || startingDate.equals(endingDate)) {


                        CoroutineScope(Dispatchers.IO).launch {
                            expenseDataArrayList = expense_data.get().await()
                                .toObjects(transactionData::class.java) as MutableList<transactionData>
                        }
//                            for(t in transactionData){
//
//                            }

//                            expenseList = transactionData
//                           expenseDataArrayList = arrayListOf()

                        tableRowAdapter = TableRowAdapter(expenseDataArrayList as ArrayList<transactionData>)
//                        tableRowAdapter = TableRowAdapter(transactionData)
                            binding.tableRecyclerView.layoutManager = LinearLayoutManager(context)
                            binding.tableRecyclerView.adapter = tableRowAdapter

//                        EventChangeListener()

//                        recycleView = binding.residentRecycleView
//                        residentProfileArrayList = arrayListOf()
//                        recycleView.layoutManager = LinearLayoutManager(context)
//                        residentProfileAdapter = residantAdapter(requireContext(),residentProfileArrayList)
//
//                        recycleView.adapter = residentProfileAdapter
//
//                        EventChangeListener()




                    } else {
                        Toast.makeText(
                            context,
                            "Please enter valid ending date!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }

            btnDownload.setOnClickListener {

            }

            btnBack.setOnClickListener {
                findNavController().navigate(expenseSheetFragmentDirections.actionExpenseSheetFragmentToDashBoardFragment())
            }
        }


    }


    fun EventChangeListener() {
        expense_data.addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("FireStore error", error.message.toString())
                    return
                }

                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {

                        expenseDataArrayList.add(dc.document.toObject(transactionData::class.java)!!)

                    }
                }
                tableRowAdapter.notifyDataSetChanged()
            }

        })
    }


}