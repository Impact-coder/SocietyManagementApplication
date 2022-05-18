package com.dhairya.societymanagementapplication.dashboardActivity.addRule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.authActivity.authfragments.ui.login.exhaustive
import com.dhairya.societymanagementapplication.dashboardActivity.rules.rulesViewModel
import com.dhairya.societymanagementapplication.databinding.FragmentAddRuleBinding
import com.dhairya.societymanagementapplication.databinding.FragmentRulesBinding
import com.google.android.material.snackbar.Snackbar

class addRuleFragment : Fragment(R.layout.fragment_add_rule) {

    private lateinit var binding: FragmentAddRuleBinding
    private val viewModel: addRuleViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddRuleBinding.bind(view)
        binding.apply {

            addRule.setText(viewModel.textRule)
            addRule.addTextChangedListener {
                viewModel.textRule = it.toString()
            }


            btnAddRuleTodatabase.setOnClickListener {
                viewModel.addRule()
            }

            btnAddRuleDone.setOnClickListener {
                findNavController().navigate(addRuleFragmentDirections.actionAddRuleFragmentToRulesFragment())
            }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addRuleEvent.collect { events ->
                when (events) {

                    is addRuleViewModel.AddRuleEvent.NavigateBackWithResult -> {
                        Toast.makeText(
                            requireContext(),
                            "Rule added Successfully!!",
                            Toast.LENGTH_SHORT
                        ).show()


                        binding.addRule.setText("")
                    }
                    is addRuleViewModel.AddRuleEvent.ShowErrorMessage -> {
                        Snackbar.make(requireView(), events.msg, Snackbar.LENGTH_LONG)
                            .show()
                    }
                }.exhaustive

            }


        }

    }
}