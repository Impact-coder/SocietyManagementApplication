package com.dhairya.societymanagementapplication.dashboardActivity.dashBoard

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.authActivity.AuthActivity
import com.dhairya.societymanagementapplication.data.noticeData
import com.dhairya.societymanagementapplication.data.profileData
import com.dhairya.societymanagementapplication.data.residentsData
import com.dhairya.societymanagementapplication.databinding.FragmentDashBoardBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_dash_board.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import java.net.URL
import java.util.*


class dashBoardFragment : Fragment(R.layout.fragment_dash_board) {

    private val viewModel: dashBoardViewModel by viewModels()
    private lateinit var binding: FragmentDashBoardBinding
    private val resident_data = FirebaseFirestore.getInstance().collection("residents")
    private val profile_data = FirebaseFirestore.getInstance().collection("profileData")


//    private lateinit var dashboardnoticeArrayList: ArrayList<noticeData>
//    private lateinit var dashboardnoticesDisplayAdapter: dashboardNoticeAdapter
//    private lateinit var recycleView: RecyclerView

    private val notice_data = FirebaseFirestore.getInstance().collection("noticeData")

    //    private lateinit var userRole: String

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding = FragmentDashBoardBinding.bind(view)
        activity?.window?.statusBarColor=Color.parseColor("#2C3454")

        weatherTask().execute()



        CoroutineScope(Dispatchers.Main).launch {

            val userName =
                profile_data.document(Firebase.auth.currentUser!!.uid).get().await()
                    .toObject(profileData::class.java)!!
            binding.dashboardName.text = userName.fullName


            val resident = resident_data.document(Firebase.auth.currentUser!!.uid).get().await()
                .toObject(residentsData::class.java)!!
//            binding.dashboardName.text = resident.email
            if (resident.role == "member") {
                binding.btnAddMember.isVisible = false
                binding.btnAddExpense.isVisible = false
                binding.btnNotices.isVisible = false
                binding.btnShowComplains.isVisible = false

            }

            if (resident.role == "treasurer") {
                binding.btnAddMember.isVisible = false
                binding.btnNotices.isVisible = false
                binding.btnShowComplains.isVisible = false
            }
        }

        binding.apply {

//                recycleView = binding.dashboardRecycleView
//                dashboardnoticeArrayList = arrayListOf()
//                recycleView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
//
            CoroutineScope(Dispatchers.Main).launch {

                val list =
                    notice_data.orderBy("dateTime", Query.Direction.ASCENDING).limit(1).get()
                        .await().toObjects(noticeData::class.java)!!
                if (list.isEmpty()) {
                    dashboardNoticeDate.text = ""
                    dashboardNoticeSubject.text = "No new Notices"

                } else {
                    dashboardNoticeDate.text = list[0].dateTime.toString()
                    dashboardNoticeSubject.text = list[0].title.toString()
                }


            }

            btn_rules.setOnClickListener {
                findNavController().navigate(dashBoardFragmentDirections.actionDashBoardFragmentToRulesFragment())
            }
            cvNotice.setOnClickListener {
                findNavController().navigate(dashBoardFragmentDirections.actionDashBoardFragmentToNoticeListFragment())
            }
            btnExpenseSheet.setOnClickListener {
                getView()?.performHapticFeedback(
                    HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                );
                findNavController().navigate(
                    dashBoardFragmentDirections.actionDashBoardFragmentToExpenseSheetFragment(),
                    null
                )
            }

            btnResidents.setOnClickListener {
                getView()?.performHapticFeedback(
                    HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                );
                findNavController().navigate(dashBoardFragmentDirections.actionDashBoardFragmentToResidentListFragment())
            }
            btnPayMaintenance.setOnClickListener {
                getView()?.performHapticFeedback(
                    HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                );
                findNavController().navigate(dashBoardFragmentDirections.actionDashBoardFragmentToMaintenanceInvoiceFragment())
            }

            btnAddMember.setOnClickListener {
                getView()?.performHapticFeedback(
                    HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                );
                findNavController().navigate(dashBoardFragmentDirections.actionDashBoardFragmentToAddMemberFragment())
            }

            btnAddExpense.setOnClickListener {
                getView()?.performHapticFeedback(
                    HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                );
                findNavController().navigate(dashBoardFragmentDirections.actionDashBoardFragmentToAddTransactionFragment())

            }

            btnCommunication.setOnClickListener {
                getView()?.performHapticFeedback(
                    HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                );
                findNavController().navigate(dashBoardFragmentDirections.actionDashBoardFragmentToCommunicationFragment())
            }


            btnNotices.setOnClickListener {
                getView()?.performHapticFeedback(
                    HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                );
                findNavController().navigate(dashBoardFragmentDirections.actionDashBoardFragmentToNoticeFragment())
            }
            btnFileComplain.setOnClickListener {
                getView()?.performHapticFeedback(
                    HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                );
                findNavController().navigate(dashBoardFragmentDirections.actionDashBoardFragmentToFileComplain())
            }


            btnShowComplains.setOnClickListener {
                getView()?.performHapticFeedback(
                    HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                );
                findNavController().navigate(dashBoardFragmentDirections.actionDashBoardFragmentToComplainsListFragment())
            }

            btnMyComplain.setOnClickListener {
                getView()?.performHapticFeedback(
                    HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                );
                findNavController().navigate(dashBoardFragmentDirections.actionDashBoardFragmentToMyComplainsFragment())
            }


            popupMenu.setOnClickListener {
                val popup = PopupMenu(context, it)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.btn_about -> {
                            findNavController().navigate(dashBoardFragmentDirections.actionDashBoardFragmentToAboutFragment())
                        }
                        R.id.btn_logout -> {
                            val builder = AlertDialog.Builder(context)
                            builder.setTitle("Logout")
                            builder.setIcon(R.drawable.ic_logout)
                            builder.setMessage("Are you Sure you want to logout")
                                .setPositiveButton("Yes") { dialogInterface, which ->
                                    Firebase.auth.signOut()
                                    Intent(requireContext(), AuthActivity::class.java).also {
                                        startActivity(it)
                                        requireActivity().finish()
                                    }
                                }
                                .setNeutralButton("Cancel") { dialogInterface, which ->

                                }
                            val alertDialog: AlertDialog = builder.create()
                            alertDialog.setCancelable(false)
                            alertDialog.show()
                        }

                    }
                    true
                }
                popup.inflate(R.menu.popup_menu)
                try {
                    val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
                    fieldMPopup.isAccessible = true
                    val mPoopup = fieldMPopup.get(popup)
                    mPoopup.javaClass
                        .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                        .invoke(mPoopup, true)
                } catch (e: Exception) {
                    Log.e("main", "Error Menu Icon")
                }
                popup.show()
            }
        }


    }


    inner class weatherTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun doInBackground(vararg params: String?): String? {
            var response: String?

            try {
                response =
                    URL("https://api.weatherapi.com/v1/forecast.json?key=4ea060d7da054edab1093832222207&q=Ahmedabad&days=1&aqi=no&alerts=no").readText(
                        Charsets.UTF_8
                    )
            } catch (e: Exception) {
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val current = jsonObj.getJSONObject("current")
                val condition = current.getJSONObject("condition")

                val temp = current.getString("temp_c")[0] + current.getString("temp_c")[1].toString() + "°C"
                txt_temp.text = temp

                val url = condition.getString("icon")
                Glide.with(requireActivity()).load("https://" + url).into(img_temp)
//                findViewById<TextView>(R.id.txt_temp).text = temp


            } catch (e: Exception) {


            }
        }


    fun showProgress(bool: Boolean) {
        binding.apply {
            animationView.isVisible = bool
            if (bool) {
                parentLayoutDashboard.alpha = 0.5f
                activity?.window!!.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            } else {
                parentLayoutDashboard.alpha = 1f
                activity?.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }
    }


}
        }