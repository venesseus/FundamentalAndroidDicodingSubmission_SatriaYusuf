package com.example.githubuserfinder.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.githubuserfinder.R
import com.example.githubuserfinder.ui.main.MainActivity

class SplashActivityMeow : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_meow)

        supportActionBar?.hide()

        val handler = Handler()
        handler.postDelayed({

            val intent = Intent(this@SplashActivityMeow, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 5000)
    }
}