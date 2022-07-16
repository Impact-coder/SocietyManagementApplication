package com.dhairya.societymanagementapplication.dashboardActivity.maintenanceInvoice

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhairya.societymanagementapplication.data.profileData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.razorpay.Checkout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.json.JSONObject

class maintenanceinvoiceViewModel(
    private val state: SavedStateHandle
):ViewModel() {

    val profile_data = FirebaseFirestore.getInstance().collection("profileData")



    private val MaintenanceInvoiceEventChannel = Channel<maintenanceinvoiceViewModel.MaintenanceInvoiceEvent>()
    val maintenanceInvoiceEvent = MaintenanceInvoiceEventChannel.receiveAsFlow()
    private fun showErrorMessage(text: String) = viewModelScope.launch {
        MaintenanceInvoiceEventChannel.send(MaintenanceInvoiceEvent.ShowErrorMessage(text))
    }

    sealed class MaintenanceInvoiceEvent {
        data class ShowErrorMessage(val msg: String) : MaintenanceInvoiceEvent()
        data class NavigateBackWithResult(val result: Int) : MaintenanceInvoiceEvent()
    }

//    fun PayMaintenance(){
//
//        val activity: maintenanceinvoiceViewModel = this
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
//            val retryObj =  JSONObject();
//            retryObj.put("enabled", true);
//            retryObj.put("max_count", 4);
//            options.put("retry", retryObj);
//            val prefill = JSONObject()
//            CoroutineScope(Dispatchers.Main).launch {
//                val userData =
//                    profile_data.document(Firebase.auth.currentUser!!.uid).get().await()
//                        .toObject(profileData::class.java)!!
//
//                prefill.put("email", userData.email)
//                prefill.put("contact",userData.mobile)
//            }
//
//
//
//            options.put("prefill",prefill)
//            co.open(this,options)
//        }catch (e: Exception){
//            showErrorMessage("Error in payment: "+ e.message)
//            e.printStackTrace()
//        }
//
//    }
}