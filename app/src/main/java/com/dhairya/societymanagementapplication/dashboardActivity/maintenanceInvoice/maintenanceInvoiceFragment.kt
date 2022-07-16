package com.dhairya.societymanagementapplication.dashboardActivity.maintenanceInvoice

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.dhairya.societymanagementapplication.R
import com.dhairya.societymanagementapplication.dashboardActivity.addMember.addMemberViewModel
import com.dhairya.societymanagementapplication.databinding.FragmentAddMemberBinding
import com.dhairya.societymanagementapplication.databinding.FragmentMaintenanceInvoiceBinding
import com.dhairya.societymanagementapplication.paymaintenance.PayMaintenance
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject


class maintenanceInvoiceFragment : Fragment(R.layout.fragment_maintenance_invoice) {

    private val viewModel: maintenanceinvoiceViewModel by viewModels()
    private lateinit var binding: FragmentMaintenanceInvoiceBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMaintenanceInvoiceBinding.bind(view)
        binding.apply {

            btnPay.setOnClickListener {
                Intent(requireContext(),PayMaintenance::class.java).also {
                    startActivity(it)
                }

            }

        }

    }

//    private fun startPayment() {
//        /*
//        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
//        * */
//        val activity:Activity = this
//        val co = Checkout()
//
//        try {
//            val options = JSONObject()
//            options.put("name","Razorpay Corp")
//            options.put("description","Demoing Charges")
//            //You can omit the image option to fetch the image from dashboard
//            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
//            options.put("theme.color", "#3399cc");
//            options.put("currency","INR");
//            options.put("order_id", "order_DBJOWzybf0sJbb");
//            options.put("amount","50000")//pass amount in currency subunits
//
//            val retryObj = JSONObject();
//            retryObj.put("enabled", true);
//            retryObj.put("max_count", 4);
//            options.put("retry", retryObj);
//
//            val prefill = JSONObject()
//            prefill.put("email","gaurav.kumar@example.com")
//            prefill.put("contact","9876543210")
//
//            options.put("prefill",prefill)
//            co.open(this,options)
//        }catch (e: Exception){
//            Toast.makeText(context,"Error in payment: "+ e.message, Toast.LENGTH_LONG).show()
//            e.printStackTrace()
//        }
//    }
//
//    override fun onPaymentSuccess(p0: String?) {
//        Toast.makeText(context, "Payment Successfully", Toast.LENGTH_SHORT).show()    }
//
//    override fun onPaymentError(p0: Int, p1: String?) {
//        Toast.makeText(context, "Payment Failed", Toast.LENGTH_SHORT).show()
//    }
//}
//
//private fun Checkout.open(maintenanceInvoiceFragment: maintenanceInvoiceFragment, options: JSONObject) {
//
}
