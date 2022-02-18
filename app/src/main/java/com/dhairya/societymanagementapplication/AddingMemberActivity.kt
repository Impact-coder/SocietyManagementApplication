package com.dhairya.societymanagementapplication

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AddingMemberActivity : AppCompatActivity() {

    // Firebase auth instance
    private val auth = FirebaseAuth.getInstance()

    private val residents = FirebaseFirestore.getInstance().collection("residents")

    var arr=ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding_member)

        setStatusBarTransparent() // call setstatusbar function
        supportActionBar?.hide()        // hides the action bar

        var listview = findViewById<ListView>(R.id.list_members)
        var add_member = findViewById<AppCompatButton>(R.id.btn_add_member)
        var member_email=findViewById<TextInputEditText>(R.id.email_edittext)
        var member_password: String = "SMA@cp2"
        var member_flatno=findViewById<TextInputEditText>(R.id.flatno_edittext)
        var btn_done = findViewById<AppCompatButton>(R.id.btn_done)

        add_member.setOnClickListener {

            if (member_email.length() == 0 || member_flatno.length() == 0) {
                Toast.makeText(this, "Please Enter required details!!", Toast.LENGTH_SHORT).show()

            } else {

                CoroutineScope(Dispatchers.Main).launch {

                    try {
                        auth.createUserWithEmailAndPassword(
                            member_email.text.toString(),
                            member_password
                        ).await()
                    } catch (e: Exception) {
                        Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }


                    if (auth.currentUser != null) {
                        val uid = auth.currentUser!!.uid
                        val resident = residentsData(
                            uid = uid,
                            email = member_email.text.toString(),
                            flatNo = member_flatno.text.toString()
                        )

                        residents.document(uid).set(resident).await()

                        Toast.makeText(
                            applicationContext,
                            "Members added Successfully!!",
                            Toast.LENGTH_SHORT
                        ).show()


                    }


                }
            }

            if(member_flatno.text.toString() == "")
            {
                Toast.makeText(this, "Please enter Email Address", Toast.LENGTH_LONG).show()
            }
            else
            {
                var details=member_email.text.toString() +"\t\t\t"+ member_flatno.text.toString()
                arr.add(details)
            }
            var adapter: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,arr)
            listview.adapter = adapter

        }

        btn_done.setOnClickListener {
            Intent(this, DashBoard::class.java).apply {
                startActivity(this)
                finish()
            }
        }

    }

    // Function to transparent status bar
    private fun setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT in 19..20){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
            }
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean){
        val winParameters=window.attributes
        if(on) {
            winParameters.flags = winParameters.flags or bits
        }else{
            winParameters.flags=winParameters.flags and bits.inv()
        }
        window.attributes=winParameters
    }




}