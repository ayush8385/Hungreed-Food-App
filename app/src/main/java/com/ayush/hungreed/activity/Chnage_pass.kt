package com.ayush.hungreed.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ayush.hungreed.R
import org.json.JSONException
import org.json.JSONObject

class Chnage_pass : AppCompatActivity() {
    lateinit var otp:EditText
    lateinit var pass:EditText
    lateinit var submit:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chnage_pass)

        otp=findViewById(R.id.otp)
        pass=findViewById(R.id.otp_newpass)
        submit=findViewById(R.id.submit_btn)

        submit.setOnClickListener {
            val url="http://13.235.250.119/v2/reset_password/fetch_result/"
            val queue=Volley.newRequestQueue(this)
            val otp=otp.text.toString()
            val password=pass.text.toString()
            val number=intent.getStringExtra("Number")

            val jsonParams=JSONObject().put("mobile_number",number).put("password",password).put("otp",otp)

            val jsonObjectRequest=object :JsonObjectRequest(Method.POST,url,jsonParams,Response.Listener {
                try {
                    val res = it.getJSONObject("data")
                    val success = res.getBoolean("success")
                    if(success){
                        Toast.makeText(applicationContext,res.getString("successMessage").toString(),Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@Chnage_pass, Login::class.java))
                        finish()
                    } else {
                        Toast.makeText(this,jsonParams.toString(), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException){
                    Toast.makeText(this, "Some unexpected error occurred!!!", Toast.LENGTH_SHORT).show()
                }
            },Response.ErrorListener {
                Toast.makeText(applicationContext,"Volley Error Occurred!!!",Toast.LENGTH_SHORT).show()
            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers=HashMap<String,String>()
                    headers["Content-type"]="application/json"
                    headers["token"]="45cd8d265f3a83"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)
        }

    }
}