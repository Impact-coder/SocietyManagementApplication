package com.dhairya.societymanagementapplication.dashboardActivity.addTransaction

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.authActivity.authfragments.ui.login.exhaustive
import com.dhairya.societymanagementapplication.dashboardActivity.addMember.addMemberViewModel
import com.dhairya.societymanagementapplication.databinding.FragmentAddTransactionBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*


class AddTransactionFragment : Fragment(R.layout.fragment_add_transaction) {

    private val viewModel: addTransactionViewModel by viewModels()
    private lateinit var binding: FragmentAddTransactionBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var cal = Calendar.getInstance()
        var date = ""

        binding = FragmentAddTransactionBinding.bind(view)
        binding.apply {
            btnSelectDate.setText("Select Date")

            btnSelectDate.setText(viewModel.btnSelectDate)
            particularsEditText.setText(viewModel.paricularasEditText)
            amountEditText.setText(viewModel.amountEditText)

            particularsEditText.addTextChangedListener {
                viewModel.paricularasEditText = it.toString()

            }

            amountEditText.addTextChangedListener {
                viewModel.amountEditText = it.toString()

            }

            btnSelectDate.addTextChangedListener {
                viewModel.btnSelectDate = it.toString()

            }


            binding.apply {
                val dateSetListener =
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, monthOfYear)
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        val myFormat = "dd/MM/yyyy" // mention the format you need
                        val sdf = SimpleDateFormat(myFormat, Locale.US)
                        btnSelectDate.textSize = 20F
                        date = sdf.format(cal.getTime())
                        btnSelectDate.text = sdf.format(cal.getTime())
                    }

                btnSelectDate.setOnClickListener {

                    DatePickerDialog(
                        btnSelectDate.context,
                        dateSetListener,
                        // set DatePickerDialog to point to today's date when it loads up
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                    ).show()

                }
                btnAddTransaction.setOnClickListener {
                    viewModel.addTransaction()
                }

                btnBack.setOnClickListener {
                    findNavController().navigate(AddTransactionFragmentDirections.actionAddTransactionFragmentToDashBoardFragment())
                }

            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.addTransactionEvent.collect { events ->
                    when (events) {

                        is addTransactionViewModel.AddTransactionEvent.NavigateBackWithResult -> {
                            Toast.makeText(
                                context,
                                "Transaction added Successfully!!",
                                Toast.LENGTH_SHORT
                            ).show()

                            binding.particularsEditText.setText("")
                            binding.amountEditText.setText("")
                        }
                        is addTransactionViewModel.AddTransactionEvent.ShowErrorMessage -> {
                            Snackbar.make(requireView(), events.msg, Snackbar.LENGTH_LONG).show()

                        }
                    }.exhaustive
                }

            }

        }

    }
}