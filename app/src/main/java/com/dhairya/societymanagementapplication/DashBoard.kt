package com.dhairya.societymanagementapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton

class DashBoard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        var add_member_btn = findViewById<AppCompatButton>(R.id.add_member_btn)

        add_member_btn.setOnClickListener {
            Intent(this, AddingMemberActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }
    }
}