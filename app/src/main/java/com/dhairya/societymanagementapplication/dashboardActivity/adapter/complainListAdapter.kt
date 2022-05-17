package com.dhairya.societymanagementapplication.dashboardActivity.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.data.complainData

class complainListAdapter(
    private val mcontext: Context,
    private  val complainArrayList: List<complainData>
) : RecyclerView.Adapter<complainListAdapter.complainsViewHolder>() {

    private var itemOnClick: ((complainData)->Unit)? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): complainListAdapter.complainsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.complain_list_item, parent, false)
        return complainsViewHolder(view)
    }

    override fun onBindViewHolder(holder: complainsViewHolder, position: Int) {
        val complain: complainData = complainArrayList[position]


        Log.d("TAG",complain.flatNO)
        Log.d("TAG",complain.complainDate)
        holder.flatNo.text = complain.flatNO
        holder.date.text = complain.complainDate
        holder.complainSub.text = complain.complainSubject

        holder.complainStatus.isVisible = complain.complainResponse.isNotEmpty()

        holder.itemView.setOnClickListener{
            itemOnClick?.let { click->
                click(complain)

            }
        }

    }

    fun setOnItemClickListner(listner:(complainData)->Unit){
        itemOnClick = listner
    }

    override fun getItemCount(): Int {
        return complainArrayList.size
    }


    class complainsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val flatNo: TextView = itemView.findViewById(R.id.complainList_flatNo)
        val date: TextView = itemView.findViewById(R.id.complainList_date)
        val complainSub:TextView = itemView.findViewById(R.id.complainList_subject)
        val complainStatus:TextView = itemView.findViewById(R.id.complainlist_status)

    }

}