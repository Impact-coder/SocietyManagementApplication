package com.dhairya.societymanagementapplication.dashboardActivity.adapter

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.dashboardActivity.residentList.residentListViewModel
import com.dhairya.societymanagementapplication.data.profileData
import com.dhairya.societymanagementapplication.data.residentsData
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.dhairya.societymanagementapplication.dashboardActivity.residentDetailsDialog.residentDetailsDialogFragment
import com.dhairya.societymanagementapplication.dashboardActivity.residentList.residentListFragment
import com.google.android.gms.common.internal.ImagesContract.URL
import java.net.URL


class residantAdapter(

    private val mcontext: Context,
    private val residentProfileList: ArrayList<profileData>

) : RecyclerView.Adapter<residantAdapter.residentViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): residantAdapter.residentViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.resident_list_item, parent, false)



        return residentViewHolder(view)
    }


    override fun onBindViewHolder(holder: residantAdapter.residentViewHolder, position: Int) {
        val resident: profileData = residentProfileList[position]

        holder.fullName.text = resident.fullName
        holder.number.text = resident.mobile

        val dialog = Dialog(mcontext)
        dialog.setContentView(R.layout.fragment_resident_details_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val name = dialog.findViewById<TextView>(R.id.residentDialog_name)
        val mobile = dialog.findViewById<TextView>(R.id.residentDialog_number)
        val email = dialog.findViewById<TextView>(R.id.residentDialog_email)
        val address = dialog.findViewById<TextView>(R.id.residentDialog_residentAddress)
        val ownershipStatus=dialog.findViewById<TextView>(R.id.residentDialog_residentStatus)
        val profilePic = dialog.findViewById<ImageView>(R.id.residentDialog_profileimg)

        holder.itemView.setOnClickListener {

            Glide.with(profilePic.context)
                .load(resident.profileImg)
                .centerCrop()
                .into(profilePic)
            name.text = residentProfileList[position].fullName
            mobile.text = residentProfileList[position].mobile
            email.text = residentProfileList[position].email
            address.text = residentProfileList[position].flatNo + ", Santiniketan Recidency, Kathodara Road, Surat-395006"
            ownershipStatus.text=residentProfileList[position].ownershipStatus

            dialog.show()
        }


        Glide.with(holder.profileImg.context)
            .load(resident.profileImg)
            .centerCrop()
            .into(holder.profileImg)

    }

    override fun getItemCount(): Int {
        return residentProfileList.size
    }

    inner class residentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val fullName: TextView = itemView.findViewById(R.id.residentListItem_name)
        val number: TextView = itemView.findViewById(R.id.residentListItem_number)
        val profileImg: ImageView = itemView.findViewById(R.id.resident_list_item_profile_img)
    }


}