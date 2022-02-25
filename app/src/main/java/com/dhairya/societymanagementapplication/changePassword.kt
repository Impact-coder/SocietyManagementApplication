package com.dhairya.societymanagementapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class changePassword : AppCompatActivity() {

    val user = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        val oldPassword = findViewById<TextInputEditText>(R.id.old_pass_edittext)
        val newPassword = findViewById<TextInputEditText>(R.id.new_pass_edittext)
        val confirmPassword = findViewById<TextInputEditText>(R.id.confirm_pass_edittext)

        val btnChangePassword = findViewById<AppCompatButton>(R.id.btnChangePassword)

        btnChangePassword.setOnClickListener {


            val credential = EmailAuthProvider
                .getCredential(user?.email!!, oldPassword.toString())

            CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
                user.reauthenticate(credential).addOnCompleteListener {
                    if (it.isSuccessful) {
                        user.updatePassword(newPassword.toString())
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(applicationContext, "Password change Successfully!!", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    Toast.makeText(applicationContext, it.exception.toString(), Toast.LENGTH_SHORT)
                                }
                            }

                    } else {
                        Toast.makeText(applicationContext, it.exception.toString(), Toast.LENGTH_SHORT)
                    }
                }
            }
        }


    }
}