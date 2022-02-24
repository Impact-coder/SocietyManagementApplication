package com.dhairya.societymanagementapplication

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class forgotPassword : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        setStatusBarTransparent()
        supportActionBar?.hide()

        var fogotPasswordEmial = findViewById<TextInputEditText>(R.id.forgot_password_email)
        var btn_forgotPassword = findViewById<AppCompatButton>(R.id.btn_forgot_password)

        btn_forgotPassword.setOnClickListener {

            var email = fogotPasswordEmial.text

            if (email?.length == 0) {
                val error = "The field must not be empty"
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            } else {
                CoroutineScope(Dispatchers.IO).launch(Dispatchers.Main) {
                    try {
                        auth.sendPasswordResetEmail(email.toString()).await()

                        Intent(applicationContext, forgotPasswordConfirmation::class.java).apply {
                            startActivity(this)
                        }
                    } catch (e: Exception) {
                        Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }


                }
            }
        }


    }

    private fun setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT in 19..20) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
            }
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val winParameters = window.attributes
        if (on) {
            winParameters.flags = winParameters.flags or bits
        } else {
            winParameters.flags = winParameters.flags and bits.inv()
        }
        window.attributes = winParameters
    }
}