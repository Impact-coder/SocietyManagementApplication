package com.dhairya.societymanagementapplication.dashboardActivity.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.data.transactionData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TableRowAdapter(
    private var expenseArrayList: ArrayList<transactionData>,
) :
    RecyclerView.Adapter<TableRowAdapter.ViewHolder>() {

    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.table_row_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

        val amt=expenseArrayList[i].amount.toString()

        viewHolder.date.text = expenseArrayList[i].date
        viewHolder.particular.text = expenseArrayList[i].particular


        if(amt[0]=='-')
        {
            viewHolder.amount.text = expenseArrayList[i].amount
            viewHolder.amount.setTextColor(Color.parseColor("#FF0000"))
        }
        else
        {
            viewHolder.amount.text = expenseArrayList[i].amount
            viewHolder.amount.setTextColor(Color.parseColor("#4cbb17"))
        }


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