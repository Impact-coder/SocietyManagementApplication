package com.dhairya.societymanagementapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class Login : AppCompatActivity() {
    // Firebase auth instance
    private val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val login_username = findViewById<TextInputEditText>(R.id.login_username)
        val login_password = findViewById<TextInputEditText>(R.id.login_password)
        val btn_login = findViewById<AppCompatButton>(R.id.btn_login)

        btn_login.setOnClickListener {

            if (login_username.text!!.isBlank() || login_password.text!!.isBlank()) {
                Toast.makeText(this, "Any field can't be empty!!", Toast.LENGTH_SHORT).show()
            } else {

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        auth.signInWithEmailAndPassword(
                            login_username.text.toString(),
                            login_password.text.toString()
                        ).await()

                        withContext(Dispatchers.Main) {
                            Intent(applicationContext, DashBoard::class.java).apply {
                                startActivity(this)
                                finish()
                            }
                        }

                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(application, e.message.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }

                    }

                }

            }


        }
    }
}