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
import org.json.JSONObject

class Forgot : AppCompatActivity() {
    lateinit var send_otp:Button
    lateinit var number:EditText
    lateinit var email:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)
        send_otp=findViewById(R.id.forg_btn)
        number=findViewById(R.id.forg_mob)
        email=findViewById(R.id.forg_email)

        send_otp.setOnClickListener {
            val number=number.text.toString()
            val email=email.text.toString()
            val url="http://13.235.250.119/v2/forgot_password/fetch_result"
            val queue=Volley.newRequestQueue(this)

            val jsonParams=JSONObject().put("mobile_number",number).put("email",email)
            val jsonObjectRequest=object :JsonObjectRequest(Method.POST,url,jsonParams,Response.Listener {
                val res=it.getJSONObject("data")
                val success=res.getBoolean("success")

                if(success){
                    val first=res.getBoolean("first_try")
                    if(first){
                        val intent= Intent(this@Forgot, Chnage_pass::class.java)
                        intent.putExtra("Number",number)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(applicationContext,"OTP already sent",Toast.LENGTH_SHORT).show()
                        val intent=Intent(this@Forgot, Chnage_pass::class.java)
                        intent.putExtra("Number",number)
                        startActivity(intent)
                        finish()
                    }
                }
                else{
                    Toast.makeText(applicationContext,"Enter Details",Toast.LENGTH_SHORT).show()
                }
            },Response.ErrorListener {
                Toast.makeText(applicationContext,"Volley Error Occurred",Toast.LENGTH_SHORT).show()
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