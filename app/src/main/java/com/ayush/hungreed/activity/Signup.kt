package com.ayush.hungreed.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ayush.hungreed.R
import com.ayush.hungreed.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class Signup : AppCompatActivity() {

    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etMobileNumber: EditText
    lateinit var etAddress: EditText
    lateinit var etPassword: EditText
    lateinit var etCnfPassword: EditText
    lateinit var btnNext : Button
    lateinit var imgBack : ImageView
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        btnNext = findViewById(R.id.button_signup)
        imgBack = findViewById(R.id.back_logo)
        etEmail = findViewById(R.id.email)
        etName = findViewById(R.id.name)
        etAddress = findViewById(R.id.delivery)
        etPassword = findViewById(R.id.pass)
        etCnfPassword = findViewById(R.id.cnf_pass)
        etMobileNumber = findViewById(R.id.mobile)

        btnNext.setOnClickListener {
            val mobileNumber = etMobileNumber.text.toString()
            val email = etEmail.text.toString()
            val name = etName.text.toString()
            val address = etAddress.text.toString()
            val pwd = etPassword.text.toString()
            val cnfPwd = etCnfPassword.text.toString()

            val intent = Intent(this, Dashboard::class.java)
            val queue = Volley.newRequestQueue(this)
            val url = "http://13.235.250.119/v2/register/fetch_result/"
            val jsonParams = JSONObject().put("name",name).put("mobile_number", mobileNumber).put("password",pwd).put("address",address).put("email",email)
            if(ConnectionManager().checkconnectivity(this)){

                val jsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {

                    try {
                        val res = it.getJSONObject("data")
                        val success = res.getBoolean("success")
                        if(success){
                            val data = res.getJSONObject("data")
                            savePreferences(data)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, res.getString("error response").toString(), Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: JSONException){
                        Toast.makeText(this, "Enter Proper Details", Toast.LENGTH_SHORT).show()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(this, "Volley error occurred!!!", Toast.LENGTH_SHORT).show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String,String> ()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "45cd8d265f3a83"
                        return headers
                    }
                }
                queue.add(jsonObjectRequest)

            } else {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("ERROR")
                dialog.setMessage("Internet Connection Not Found")
                dialog.setPositiveButton("Open Setting"){text, listener ->
                    val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingIntent)
                    finish()
                }
                dialog.setNegativeButton("Exit"){text, listener ->
                    ActivityCompat.finishAffinity(this)
                }
                dialog.create().show()
            }
        }

        imgBack.setOnClickListener {
            val intent = Intent(this, user::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun savePreferences(data: JSONObject) {
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
        sharedPreferences.edit().putString("user_id",data.getString("user_id")).apply()
        sharedPreferences.edit().putString("name",data.getString("name")).apply()
        sharedPreferences.edit().putString("email",data.getString("email")).apply()
        sharedPreferences.edit().putString("mobile_number",data.getString("mobile_number")).apply()
        sharedPreferences.edit().putString("address",data.getString("address")).apply()
    }
}
