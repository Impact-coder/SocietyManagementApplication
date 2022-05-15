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


        holder.itemView.setOnClickListener{
            itemOnClick?.let { click->
                click(complain)

            }
        }

//        val dialog = Dialog(mcontext)
//        dialog.setContentView(R.layout.fragment_resident_details_dialog)
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//        val name = dialog.findViewById<TextView>(R.id.residentDialog_name)
//        val mobile = dialog.findViewById<TextView>(R.id.residentDialog_number)
//        val email = dialog.findViewById<TextView>(R.id.residentDialog_email)
//        val address = dialog.findViewById<TextView>(R.id.residentDialog_residentAddress)
//        val ownershipStatus=dialog.findViewById<TextView>(R.id.residentDialog_residentStatus)
//        val profilePic = dialog.findViewById<ImageView>(R.id.residentDialog_profileimg)
//        val call=dialog.findViewById<AppCompatButton>(R.id.btn_call)
//        val msg=dialog.findViewById<AppCompatButton>(R.id.btn_message)
//
//        call.setOnClickListener {
//            Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:"+mobile.text)).also {
//                ContextCompat.startActivity(mcontext, it, null)
//            }
//        }
//
//        holder.itemView.setOnClickListener {
//
//            name.text = residentProfileList[position].fullName
//            mobile.text = residentProfileList[position].mobile
//            email.text = residentProfileList[position].email
//            address.text = residentProfileList[position].flatNo + ", Santiniketan Recidency, Kathodara Road, Surat-395006"
//            ownershipStatus.text=residentProfileList[position].ownershipStatus
//
//            dialog.show()
//        }


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

    }

}