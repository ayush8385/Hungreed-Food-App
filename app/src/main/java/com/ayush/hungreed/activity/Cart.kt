package com.ayush.hungreed.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ayush.hungreed.R
import com.ayush.hungreed.adapter.CartrecyclerAdapter
import com.ayush.hungreed.model.Dish
import com.ayush.hungreed.util.ConnectionManager
import org.json.JSONArray
import org.json.JSONObject

class Cart : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: CartrecyclerAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var orderName:TextView
    lateinit var place_order:Button
    lateinit var dishArray:JSONArray
    lateinit var sharedPreferences: SharedPreferences
    var total:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        recyclerView=findViewById(R.id.recycler_cart)
        orderName=findViewById(R.id.order_from_name)
        place_order=findViewById(R.id.place_order_btn)
        sharedPreferences=getSharedPreferences(getString(R.string.preference_file_name),Context.MODE_PRIVATE)
        dishArray= JSONArray()
        layoutManager=LinearLayoutManager(this)

        if(intent!=null){
            val orderList=intent.getParcelableArrayListExtra<Dish>("Orders")
            orderName.text=intent.getStringExtra("resName")

            for(i in orderList!!){
                total+=i.foodPrice?.toInt()?:0
                val obj = JSONObject().put("food_item_id",i.foodId)
                dishArray.put(obj)
            }
            place_order.text="Place Order(Total \u20B9$total)"

            recyclerAdapter= CartrecyclerAdapter(this,orderList)
            recyclerView.adapter=recyclerAdapter
            recyclerView.layoutManager=layoutManager

            place_order.setOnClickListener {
                val queue = Volley.newRequestQueue(this)
                val url = "http://13.235.250.119/v2/place_order/fetch_result/"

                val jsonParams=JSONObject()
                    .put("user_id",sharedPreferences.getString("user_id","0"))
                    .put("restaurant_id",orderList[0].restaurantId)
                    .put("total_cost",total)
                    .put("food",dishArray)

                if(ConnectionManager().checkconnectivity(this)){
                    val jsonObjectRequest=object :JsonObjectRequest(Method.POST,url,jsonParams,Response.Listener {
                        try{
                            val res=it.getJSONObject("data")
                            val success=res.getBoolean("success")
                            if(success){
                                finish()
                                startActivity(Intent(this@Cart,Final::class.java))
                            }
                            else{
                                Toast.makeText(this,"Error While placing Order", Toast.LENGTH_LONG).show()
                            }
                        }
                        catch (e:Exception){
                            Toast.makeText(applicationContext,"Error Occurred",Toast.LENGTH_SHORT).show()
                        }
                    },Response.ErrorListener {
                        Toast.makeText(this, "Volley error occurred!!!", Toast.LENGTH_SHORT).show()
                    }){
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers=HashMap<String,String>()
                            headers["Content-type"]="applicatiopn/json"
                            headers["token"]="45cd8d265f3a83    "
                            return headers
                        }
                    }
                    queue.add(jsonObjectRequest)
                }
                else{
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
        }


    }
}