package com.ayush.hungreed.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.ayush.hungreed.R

class Final : AppCompatActivity() {
    lateinit var placed:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final)

        placed=findViewById(R.id.placed_ok)

        placed.setOnClickListener {
            startActivity(Intent(this@Final,Dashboard::class.java))
            finishAffinity()
        }
    }
}