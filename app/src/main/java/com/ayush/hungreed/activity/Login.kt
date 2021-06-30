package com.ayush.hungreed.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ayush.hungreed.R
import com.ayush.hungreed.util.ConnectionManager
import org.json.JSONObject

class Login : AppCompatActivity() {
    lateinit var forgot:TextView
    lateinit var back:ImageView
    lateinit var mob_num:EditText
    lateinit var password:EditText
    lateinit var login_btn: Button
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences=getSharedPreferences(getString(R.string.preference_file_name),Context.MODE_PRIVATE)
        forgot=findViewById(R.id.forgot)
        back=findViewById(R.id.back_logo)
        mob_num=findViewById(R.id.mobile)
        password=findViewById(R.id.pass)
        login_btn=findViewById(R.id.button_login)


        login_btn.setOnClickListener {
            val number=mob_num.text.toString()
            val pass_word=password.text.toString()
            val url="http://13.235.250.119/v2/login/fetch_result/"
            val queue=Volley.newRequestQueue(this)

            if(ConnectionManager().checkconnectivity(this)){

                val jsonParams=JSONObject().put("mobile_number",number).put("password",pass_word)
                val jsonObjectRequest=object :JsonObjectRequest(Method.POST,url,jsonParams,Response.Listener {
                    val res=it.getJSONObject("data")
                    val success=res.getBoolean("success")

                    if(success){
                        val data=res.getJSONObject("data")
                        savePreferences(data)
                        finishAffinity()
                        startActivity(Intent(this@Login, Dashboard::class.java))
                        finish()
                    }
                    else{
                        Toast.makeText(applicationContext,"Enter Proper Details",Toast.LENGTH_SHORT).show()
                    }

                },Response.ErrorListener {
                    Toast.makeText(applicationContext,"Volley Error",Toast.LENGTH_SHORT).show()
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
            else{
                val dialog=AlertDialog.Builder(applicationContext)
                dialog.setTitle("Error")
                dialog.setMessage("Internet is not connected")
                dialog.setPositiveButton("Ok"){text,Listener->
                    startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                }
                dialog.setNegativeButton("Exit"){text,Listener->
                    ActivityCompat.finishAffinity(this as Activity)
                }
                dialog.create().show()
            }
        }

        forgot.setOnClickListener {
            if(ConnectionManager().checkconnectivity(this)){
                startActivity(Intent(applicationContext, Forgot::class.java))
            }
            else{
                val dialog=AlertDialog.Builder(applicationContext)
                dialog.setTitle("Error")
                dialog.setMessage("Internet is not connected")
                dialog.setPositiveButton("Ok"){text,Listener->
                    startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                }
                dialog.setNegativeButton("Exit"){text,Listener->
                    ActivityCompat.finishAffinity(this as Activity)
                }
                dialog.create().show()
            }
        }
        back.setOnClickListener{
            startActivity(Intent(applicationContext, user::class.java))
            finish()
        }
    }
    fun savePreferences(data:JSONObject){
        sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()
        sharedPreferences.edit().putString("user_id",data.getString("user_id")).apply()
        sharedPreferences.edit().putString("name",data.getString("name")).apply()
        sharedPreferences.edit().putString("email",data.getString("email")).apply()
        sharedPreferences.edit().putString("mobile_number",data.getString("mobile_number")).apply()
        sharedPreferences.edit().putString("address",data.getString("address")).apply()
    }
}