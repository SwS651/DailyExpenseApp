package com.assginment.dailyexpense3

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Looper
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        sharedPreferences = getSharedPreferences("appPreferences", MODE_PRIVATE)
        val skipWelcomeScreen = sharedPreferences.getBoolean("skipWelcomeScreen", false)

        if (skipWelcomeScreen) {
            navigateToMain()
        }else{

            findViewById<Button>(R.id.splash_skip).setOnClickListener {
                sharedPreferences.edit().putBoolean("skipWelcomeScreen", true).apply()
                navigateToMain()
                finish()
            }
            android.os.Handler(Looper.getMainLooper()).postDelayed({
                navigateToMain()
            }, 10000) // Delay of 10 seconds

        }

    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}