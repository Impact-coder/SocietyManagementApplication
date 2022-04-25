package com.dhairya.societymanagementapplication.dashboardActivity.addTransaction

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.navigation.fragment.findNavController
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.dashboardActivity.addMember.addMemberFragmentDirections
import com.dhairya.societymanagementapplication.databinding.FragmentAddTransactionBinding
import com.dhairya.societymanagementapplication.databinding.FragmentExpenseSheetBinding
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddTransactionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddTransactionFragment : Fragment(R.layout.fragment_add_transaction) {

    private lateinit var binding : FragmentAddTransactionBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var cal = Calendar.getInstance()
        var date=""

        binding = FragmentAddTransactionBinding.bind(view)
        binding.apply {

            val dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val myFormat = "dd/MM/yyyy" // mention the format you need
                    val sdf = SimpleDateFormat(myFormat, Locale.US)
                    btnSelectDate.textSize= 20F
                    date=sdf.format(cal.getTime())
                    btnSelectDate.text = sdf.format(cal.getTime())
                }

            btnSelectDate.setOnClickListener {
                DatePickerDialog(requireContext(),
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

            btnBack.setOnClickListener {
                findNavController().navigate(AddTransactionFragmentDirections.actionAddTransactionFragmentToDashBoardFragment())
            }

        }

    }

}