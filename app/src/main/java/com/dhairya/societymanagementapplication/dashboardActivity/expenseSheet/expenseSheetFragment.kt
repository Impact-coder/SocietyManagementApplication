package com.dhairya.societymanagementapplication.dashboardActivity.expenseSheet

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.databinding.FragmentExpenseSheetBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*


class expenseSheetFragment : Fragment(R.layout.fragment_expense_sheet) {

    private val viewModel: expenseSheetViewModel by viewModels()
    private lateinit var binding: FragmentExpenseSheetBinding

    private var expense_data = FirebaseFirestore.getInstance().collection("transactionData")
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

                    var startingDate:Date = sdf.parse(sDate)
                    var endingDate:Date = sdf.parse(eDate)

                    if (startingDate.before(endingDate) || startingDate.equals(endingDate))
                    {

                    }
                    else{
                        Toast.makeText(context, "Please enter valid ending date!!", Toast.LENGTH_SHORT).show()
                    }

                }
            }

            btnBack.setOnClickListener {
                findNavController().navigate(expenseSheetFragmentDirections.actionExpenseSheetFragmentToDashBoardFragment())
            }
        }

    }

}