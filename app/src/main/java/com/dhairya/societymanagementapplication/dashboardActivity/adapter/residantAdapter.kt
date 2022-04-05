package com.dhairya.societymanagementapplication.dashboardActivity.adapter

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
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.google.android.gms.common.internal.ImagesContract.URL
import java.net.URL


class residantAdapter(

    private val residentProfileList: ArrayList<profileData>

) : RecyclerView.Adapter<residantAdapter.residentViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): residantAdapter.residentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.resident_list_item, parent, false
        )

        return residentViewHolder(view)
    }

    override fun onBindViewHolder(holder: residantAdapter.residentViewHolder, position: Int) {
        val resident: profileData = residentProfileList[position]

        holder.fullName.text = resident.fullName
        holder.number.text = resident.mobile

//        val url = URL(resident.profileImg)
//        val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
//        holder.profileImg.setImageBitmap(bmp)

        Glide.with(holder.profileImg.context)
            .load(resident.profileImg)
            .centerCrop()
            .into(holder.profileImg)

           // .error(R.drawable.imagenotfound)

    }

    override fun getItemCount(): Int {
        return residentProfileList.size
    }

    public class residentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val fullName: TextView = itemView.findViewById(R.id.residentListItem_name)
        val number: TextView = itemView.findViewById(R.id.residentListItem_number)
        val profileImg: ImageView = itemView.findViewById(R.id.resident_list_item_profile_img)
    }


}