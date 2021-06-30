package com.ayush.hungreed.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.ayush.hungreed.R

class user : AppCompatActivity() {
    lateinit var login: Button
    lateinit var signup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        login=findViewById(R.id.loginbtn)
        signup=findViewById(R.id.signbtn)

        login.setOnClickListener {
            startActivity(Intent(applicationContext, Login::class.java))
        }

        signup.setOnClickListener {
            startActivity(Intent(applicationContext, Signup::class.java))
        }
    }
}