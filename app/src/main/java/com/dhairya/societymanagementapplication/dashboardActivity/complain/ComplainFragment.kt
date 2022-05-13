package com.dhairya.societymanagementapplication.dashboardActivity.complain

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.dhairya.societymanagementapplication.R




class ComplainFragment : Fragment() {
private val args by navArgs<ComplainFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val complainData = args.complain




    }


}