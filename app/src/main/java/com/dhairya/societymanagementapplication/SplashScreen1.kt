package com.dhairya.societymanagementapplication

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatButton
import com.dhairya.societymanagementapplication.authActivity.AuthActivity

class SplashScreen1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen1)
        setStatusBarTransparent()
        supportActionBar?.hide()

        val preferences:SharedPreferences=getSharedPreferences("PREFERENCE", MODE_PRIVATE)
        val FirstTime: String? =preferences.getString("FirstTimeInstall","")

        if(FirstTime.equals("Yes"))
        {

            Intent(this, AuthActivity::class.java).apply {
                startActivity(this)
                this@SplashScreen1.finish()
            }
        }
        else{
            val editor:SharedPreferences.Editor=preferences.edit()
            editor.putString("FirstTimeInstall","Yes")
            editor.apply()
        }

        var intro1_next_btn = findViewById<AppCompatButton>(R.id.intro1_next_btn)

        intro1_next_btn.setOnClickListener {
            Intent(this, SplashScreen2::class.java).apply {
                startActivity(this)
                this@SplashScreen1.finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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