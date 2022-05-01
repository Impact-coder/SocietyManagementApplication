package com.dhairya.societymanagementapplication.dashboardActivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.data.transactionData

class TableRowAdapter(private var expenseArrayList: ArrayList<transactionData>) :
    RecyclerView.Adapter<TableRowAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.table_row_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.date.text = expenseArrayList[i].date
        viewHolder.particular.text = expenseArrayList[i].particular.toString()
        viewHolder.amount.text = "Rs. "+expenseArrayList[i].amount
    }

    override fun getItemCount(): Int {
        return expenseArrayList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.date)
        val particular: TextView = itemView.findViewById(R.id.particular)
        val amount: TextView = itemView.findViewById(R.id.amount)


    }
}