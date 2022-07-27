package com.dhairya.societymanagementapplication.paymaintenance

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.dashboardActivity.DashboardActivity
import com.dhairya.societymanagementapplication.data.profileData
import com.google.android.gms.maps.model.Dash
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.json.JSONObject

class PayMaintenance : AppCompatActivity(), PaymentResultListener {

    private val co = Checkout()
    lateinit var maintenanceAmount: Number
    var userEmail: String = ""
    var userMobile: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_maintenance)
        maintenanceAmount = intent.getStringExtra("Maintenance Amount")!!.toInt() * 100
        userMobile = intent.getStringExtra("mobile")!!
        userEmail = intent.getStringExtra("email")!!


//        CoroutineScope(Dispatchers.Main).launch {
//            userData = profile_data.document(Firebase.auth.currentUser!!.uid).get().await()
//                .toObject(profileData::class.java)!!
//        }
        startPayment()
    }

    private fun startPayment() {
/*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */
        val checkOut = Checkout()
        checkOut.setKeyID("rzp_test_qp1nYX07WJgsCW")
        try {
            val options = JSONObject()
            options.put("name", "Santinikean Recidency")
            options.put("description", "Demoing Charges")
            //You can omit the image option to fetch the image from dashboard
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", maintenanceAmount.toString())//pass amount in currency subunits

            val retryObj = JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            val prefill = JSONObject()
            prefill.put("email", userEmail)
            prefill.put("contact", userMobile)

            options.put("prefill", prefill)
            checkOut.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {

        Toast.makeText(this, "Payment Successfully!!", Toast.LENGTH_SHORT).show()
        Intent(this,DashboardActivity::class.java).apply {
            startActivity(this)
        }
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment Failed !!", Toast.LENGTH_SHORT).show()
        Log.d("TAG_PAYMENT_ERROR", "onPaymentError: Code: $p0 \nMessage $p1")
    }
}