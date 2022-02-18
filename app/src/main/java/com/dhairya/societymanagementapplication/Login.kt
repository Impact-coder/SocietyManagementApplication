package com.dhairya.societymanagementapplication

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import kotlinx.coroutines.withContext

class Login : AppCompatActivity() {
    // Firebase auth instance
    private val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        setStatusBarTransparent() // call setstatusbar function
        supportActionBar?.hide()        // hides the action bar





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