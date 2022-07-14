package com.dhairya.societymanagementapplication.dashboardActivity.rules

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.dashboardActivity.adapter.rulesAdapter
import com.dhairya.societymanagementapplication.data.residentsData
import com.dhairya.societymanagementapplication.data.ruleData
import com.dhairya.societymanagementapplication.databinding.FragmentRulesBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class rulesFragment : Fragment(R.layout.fragment_rules) {

    private lateinit var binding: FragmentRulesBinding
    private val viewModel: rulesViewModel by viewModels()

    private lateinit var ruleArrayList: ArrayList<ruleData>
    private lateinit var ruleDisplayAdapter: rulesAdapter
    private lateinit var recycleView: RecyclerView

    private val rule_data = FirebaseFirestore.getInstance().collection("ruleData")

    val resident_data = FirebaseFirestore.getInstance().collection("residents")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRulesBinding.bind(view)
        binding.apply {

            recycleView = binding.ruleRecycleView
            ruleArrayList = arrayListOf()
            recycleView.layoutManager = LinearLayoutManager(context)

            CoroutineScope(Dispatchers.Main).launch {

                val list = rule_data.get().await()
                    .toObjects(ruleData::class.java)

                ruleDisplayAdapter = rulesAdapter(
                    requireContext(),
                    list.toList()
                )
                recycleView.adapter = ruleDisplayAdapter

                val residentList =
                    resident_data.document(Firebase.auth.currentUser!!.uid).get().await().toObject(
                        residentsData::class.java
                    )!!



                if (residentList.role == "Secretary") {
                        btnAddRule.setAlpha(.5f)
                }
                else
                {

                    btnAddRule.setAlpha(0f)
                }

            }




            btnBack.setOnClickListener {
                findNavController().navigate(rulesFragmentDirections.actionRulesFragmentToDashBoardFragment())
            }

            btnAddRule.setOnClickListener {

                findNavController().navigate(rulesFragmentDirections.actionRulesFragmentToAddRuleFragment())
//                val dialog = Dialog(requireContext())
//                dialog.setContentView(R.layout.add_rule_dialogbox)
//                val textRule = dialog.findViewById<TextInputEditText>(R.id.add_rule)
//                val addRuleToDB = dialog.findViewById<TextInputEditText>(R.id.btn_add_rule_todatabase)
//
//                textRule.setText(viewModel.textRule)
//
//
//                textRule.addTextChangedListener {
//                    viewModel.textRule = it.toString()
//                }
//
//
//                addRuleToDB.setOnClickListener {
//                    viewModel.addRule()
//                }
//
//
//                dialog.show()



            }
        }
    }

}