package com.dhairya.societymanagementapplication.dashboardActivity.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.data.noticeData

class dashboardNoticeAdapter(
    private val mcontext: Context,
    private val noticeList: List<noticeData>
) : RecyclerView.Adapter<dashboardNoticeAdapter.noticeViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): dashboardNoticeAdapter.noticeViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.dashboard_notice_list, parent, false)

        return noticeViewHolder(view)
    }


    override fun onBindViewHolder(holder: dashboardNoticeAdapter.noticeViewHolder, position: Int) {
        val notice: noticeData = noticeList[position]

        holder.subject.text = notice.title
        holder.date.text = notice.dateTime

        val dialog = Dialog(mcontext)
        dialog.setContentView(R.layout.dashboard_notice_dialogbox)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val dialogSubject = dialog.findViewById<TextView>(R.id.dashnoticeDialog_subject)
        val dialogDate = dialog.findViewById<TextView>(R.id.dashnoticeDialog_date)
        val dialogNotice = dialog.findViewById<TextView>(R.id.dashnoticeDialog_notice)

        holder.itemView.setOnClickListener {
            dialogSubject.text = noticeList[position].title
            dialogDate.text = noticeList[position].dateTime
            dialogNotice.text = noticeList[position].message

            dialog.show()
        }

    }

    override fun getItemCount(): Int {
        return noticeList.size
    }

    inner class noticeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val subject: TextView = itemView.findViewById(R.id.dashNoticeList_subject)
        val date: TextView = itemView.findViewById(R.id.dashNoticeList_date)

    }

}