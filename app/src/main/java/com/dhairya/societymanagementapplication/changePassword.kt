package com.dhairya.societymanagementapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.textfield.TextInputEditText

class changePassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        val oldPassword = findViewById<TextInputEditText>(R.id.old_pass_edittext)
        val newPassword = findViewById<TextInputEditText>(R.id.new_pass_edittext)
        val confirmPassword = findViewById<TextInputEditText>(R.id.confirm_pass_edittext)


    }
}