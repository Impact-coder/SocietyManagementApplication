package com.dhairya.societymanagementapplication.dashboardActivity.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.data.noticeData
import com.dhairya.societymanagementapplication.data.profileData

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
        holder.arrow.isVisible = false

        holder.subject.text = notice.title
        holder.date.text = notice.dateTime
        holder.message.text = notice.message


        holder.itemView.setOnClickListener {
            holder.message.isVisible = true
            holder.arrow.isVisible = true

        }

        holder.arrow.setOnClickListener {
            holder.message.isVisible = false
            holder.arrow.isVisible = false
        }
    }

    override fun getItemCount(): Int {
        return noticeList.size
    }

    inner class noticeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val subject: TextView = itemView.findViewById(R.id.noticeList_subject)
        val date: TextView = itemView.findViewById(R.id.noticeList_date)
        val message: TextView = itemView.findViewById(R.id.expandable_textview)
        val arrow:ImageView = itemView.findViewById(R.id.arrow)
    }


}