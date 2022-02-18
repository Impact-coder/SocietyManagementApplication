package com.dhairya.societymanagementapplication

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

class AddingMemberActivity : AppCompatActivity() {
    var arr=ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding_member)

        setStatusBarTransparent() // call setstatusbar function
        supportActionBar?.hide()        // hides the action bar

        var listview = findViewById<ListView>(R.id.list_members)
        var add_member = findViewById<AppCompatButton>(R.id.btn_add_member)
        var member_email=findViewById<TextInputEditText>(R.id.email_edittext)
        var member_flatno=findViewById<TextInputEditText>(R.id.flatno_edittext)
        add_member.setOnClickListener {
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