package com.ayush.hungreed.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.ayush.hungreed.R

class Splash : AppCompatActivity() {
    lateinit var sharedPreferences:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sharedPreferences=getSharedPreferences(getString(R.string.preference_file_name),Context.MODE_PRIVATE)

        val isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false)

        Handler().postDelayed({
            if(isLoggedIn){
                startActivity(Intent(this@Splash, Dashboard::class.java))
                finish()
            }
            else{
                startActivity(Intent(this@Splash, user::class.java))
                finish()
            }
        },1000)
    }
}