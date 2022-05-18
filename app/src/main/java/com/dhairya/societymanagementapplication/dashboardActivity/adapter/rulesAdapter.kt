package com.dhairya.societymanagementapplication.dashboardActivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.data.ruleData

class rulesAdapter(
    private val mcontext: Context,

    private val ruleList: List<ruleData>

) : RecyclerView.Adapter<rulesAdapter.ruleViewHolder>() {

    val no: Number = 1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): rulesAdapter.ruleViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rulelist_item, parent, false)

        return ruleViewHolder(view)
    }


    override fun onBindViewHolder(holder: rulesAdapter.ruleViewHolder, position: Int) {
        val rule: ruleData = ruleList[position]
        holder.textRule.text = rule.rule
        holder.ruleNumber.text = (position + 1).toString()


    }

    override fun getItemCount(): Int {
        return ruleList.size
    }

    inner class ruleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textRule: TextView = itemView.findViewById(R.id.rule)
        val ruleNumber: TextView = itemView.findViewById(R.id.rule_no)

    }

}

