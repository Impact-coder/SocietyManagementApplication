package com.dhairya.societymanagementapplication.dashboardActivity.expenseSheet

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.navigation.fragment.findNavController
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.databinding.FragmentDashBoardBinding
import com.dhairya.societymanagementapplication.databinding.FragmentExpenseSheetBinding
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class expenseSheetFragment : Fragment(R.layout.fragment_expense_sheet) {

    private lateinit var binding : FragmentExpenseSheetBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        var cal = Calendar.getInstance()

        binding = FragmentExpenseSheetBinding.bind(view)
        binding.apply {

            val dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val myFormat = "dd/MM/yyyy" // mention the format you need
                    val sdf = SimpleDateFormat(myFormat, Locale.US)
                    startDate.textSize= 17F
                    startDate.text = sdf.format(cal.getTime())
                }

            startDate.setOnClickListener {
                    DatePickerDialog(requireContext(),
                        dateSetListener,
                        // set DatePickerDialog to point to today's date when it loads up
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show()
            }
            endDate.setOnClickListener {
                DatePickerDialog(requireContext(),
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

            btnBack.setOnClickListener {
                findNavController().navigate(expenseSheetFragmentDirections.actionExpenseSheetFragmentToDashBoardFragment())
            }

        }

    }

}