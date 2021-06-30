package com.ayush.hungreed.activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ayush.hungreed.R
import com.ayush.hungreed.adapter.ItemsrecyclerAdapter
import com.ayush.hungreed.database.FoodDatabase
import com.ayush.hungreed.database.FoodEntity
import com.ayush.hungreed.model.Dish
import com.ayush.hungreed.util.ConnectionManager

class FoodItems : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter:ItemsrecyclerAdapter
    lateinit var layoutManager:RecyclerView.LayoutManager
    lateinit var drawerLayout: DrawerLayout
    lateinit var frameLayout: FrameLayout
    lateinit var proceed:Button
    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar:ProgressBar
    var res_name:String?=null
    var res_id:String?=null
    var flag=0
    var fooditems= arrayListOf<Dish>()
    var orderItems= arrayListOf<Dish>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_items)

        frameLayout=findViewById(R.id.framelayout)
        recyclerView=findViewById(R.id.recycleritems)
        progressLayout=findViewById(R.id.progresslayout)
        progressBar=findViewById(R.id.progressbar)
        proceed=findViewById(R.id.proceed_cart_btn)

        progressLayout.visibility= View.VISIBLE
        layoutManager= LinearLayoutManager(this)

        res_name=intent.getStringExtra("name")
        res_id=intent.getStringExtra("id")
        setuptoolbar()

        val url="http://13.235.250.119/v2/restaurants/fetch_result/$res_id/"
        val queue=Volley.newRequestQueue(this)

        if(ConnectionManager().checkconnectivity(this)){
            val jsonObjectRequest=object:JsonObjectRequest(Method.GET,url,null, Response.Listener {
                try{
                    progressLayout.visibility=View.GONE
                    val res=it.getJSONObject("data")
                    val success=res.getBoolean("success")
                    if(success){
                        val data=res.getJSONArray("data")
                        for(i in 0 until data.length()){
                            val itemjsonObjectRequest=data.getJSONObject(i)
                            val itemjsonRequest=Dish(
                                itemjsonObjectRequest.getString("id"),
                                itemjsonObjectRequest.getString("name"),
                                itemjsonObjectRequest.getString("cost_for_one"),
                                itemjsonObjectRequest.getString("restaurant_id")
                            )
                            fooditems.add(itemjsonRequest)
                            recyclerAdapter= ItemsrecyclerAdapter(this@FoodItems,fooditems,orderItems)

                            recyclerView.layoutManager=layoutManager
                            recyclerView.adapter=recyclerAdapter
                        }
                    }
                    else{
                        Toast.makeText(applicationContext,"Error occurred",Toast.LENGTH_SHORT).show()
                    }
                }
                catch (e:Exception){
                    Toast.makeText(applicationContext,"Some Unexpected Error occurred",Toast.LENGTH_SHORT).show()
                }
            },Response.ErrorListener {
                Toast.makeText(applicationContext,"Volley Error occurred",Toast.LENGTH_SHORT).show()
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

        proceed.setOnClickListener {
            if(orderItems.size>0){
                startActivity(Intent(this@FoodItems,Cart::class.java).putParcelableArrayListExtra("Orders",orderItems).putExtra("resName",res_name))
            }
            else{
                Toast.makeText(applicationContext,"Add atleast 1 Dish",Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun setuptoolbar(){
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title=res_name
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class DBAsyncTask(val context: Context,val foodEntity: FoodEntity,val mode:Int):AsyncTask<Void,Void,Boolean>(){
        override fun doInBackground(vararg params: Void?): Boolean {

            val db= Room.databaseBuilder(context,FoodDatabase::class.java,"food-db").build()

            when(mode){
                1->{
                    val food:FoodEntity?=db.foodDao().getRestaurantsbyId(foodEntity.restaurantId)
                    db.close()
                    return food!=null
                }
                2->{
                    db.foodDao().insertRestaurant(foodEntity)
                    db.close()
                    return true
                }
                3->{
                    db.foodDao().deleteRestaurant(foodEntity)
                    db.close()
                    return true
                }
            }

            return false
        }
    }

}