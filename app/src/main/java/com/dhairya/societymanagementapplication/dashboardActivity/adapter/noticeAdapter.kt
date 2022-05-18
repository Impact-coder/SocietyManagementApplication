package com.dhairya.societymanagementapplication.dashboardActivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.data.noticeData

class noticeAdapter(
    private val mcontext: Context,
    private val noticeList: List<noticeData>

) : RecyclerView.Adapter<noticeAdapter.noticeViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): noticeAdapter.noticeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.notice_list_item, parent, false)

        return noticeViewHolder(view)
    }


    override fun onBindViewHolder(holder: noticeAdapter.noticeViewHolder, position: Int) {
        val notice: noticeData = noticeList[position]

        holder.subject.text = notice.title
        holder.date.text = notice.dateTime
        holder.message.text = notice.message


        holder.itemView.setOnClickListener {
            holder.message.isVisible = true
            holder.arrow.animate().rotation(360f).start()

        }

        holder.arrow.setOnClickListener {
            if(holder.message.isVisible)
            {
                holder.message.isVisible = false
                holder.arrow.animate().rotation(180f).start()
            }
            else
            {
                holder.message.isVisible = true
                holder.arrow.animate().rotation(360f).start()
            }

        }

    }

    override fun getItemCount(): Int {
        return noticeList.size
    }

    inner class noticeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val subject: TextView = itemView.findViewById(R.id.dashNoticeList_subject)
        val date: TextView = itemView.findViewById(R.id.dashNoticeList_date)
        val message: TextView = itemView.findViewById(R.id.expandable_textview)
        val arrow:ImageView = itemView.findViewById(R.id.arrow)
    }

}