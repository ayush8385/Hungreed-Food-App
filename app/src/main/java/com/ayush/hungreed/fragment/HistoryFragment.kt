package com.ayush.hungreed.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ayush.hungreed.R
import com.ayush.hungreed.adapter.HistoryrecyclerAdapter
import com.ayush.hungreed.model.Items
import com.ayush.hungreed.model.Orders
import com.ayush.hungreed.util.ConnectionManager

class HistoryFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager:RecyclerView.LayoutManager
    lateinit var recyclerAdapter:HistoryrecyclerAdapter
    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var sharedPreferences: SharedPreferences
    var orders= arrayListOf<Orders>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_history, container, false)

        progressLayout=view.findViewById(R.id.progresslayout)
        progressBar=view.findViewById(R.id.progressbar)
        recyclerView=view.findViewById(R.id.recyclerhistory)
        layoutManager=LinearLayoutManager(activity as Context)
        progressLayout.visibility=View.VISIBLE
        sharedPreferences= activity?.getSharedPreferences(getString(R.string.preference_file_name),Context.MODE_PRIVATE)!!

        if(ConnectionManager().checkconnectivity(activity as Context)){
            val url="http://13.235.250.119/v2/orders/fetch_result/"+sharedPreferences.getString("user_id","0")
            val queue=Volley.newRequestQueue(activity as Context)

            val jsonObjectRequest=object :JsonObjectRequest(Method.GET,url,null,Response.Listener {
                try{
                    progressLayout.visibility=View.GONE
                    val res=it.getJSONObject("data")
                    val success=res.getBoolean("success")
                    if(success){
                        val data=res.getJSONArray("data")
                        for(i in 0 until data.length()){

                            var item_list= arrayListOf<Items>()

                            val details=data.getJSONObject(i)
                            val temp=details.getJSONArray("food_items")

                            for(j in 0 until temp.length()){
                                val dish=temp.getJSONObject(j)
                                val itemslist=Items(
                                    dish.getString("name")
                                )
                                item_list.add(itemslist)
                            }

                            val itemObject=Orders(
                                details.getString("restaurant_name"),
                                details.getString("total_cost"),
                                details.getString("order_placed_at"),
                                item_list
                            )
                            orders.add(itemObject)

                            recyclerAdapter= HistoryrecyclerAdapter(activity as Context,orders)
                            recyclerView.adapter=recyclerAdapter
                            recyclerView.layoutManager=layoutManager

                        }
                    }
                    else{
                        Toast.makeText(activity as Context,"Error while fetching data",Toast.LENGTH_SHORT).show()

                    }
                }
                catch (e:Exception){
                    Toast.makeText(activity as Context,"Error Occurred",Toast.LENGTH_SHORT).show()
                }
            },Response.ErrorListener {
                Toast.makeText(activity as Context,"Volley Error Occurred",Toast.LENGTH_SHORT).show()
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
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("ERROR")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Setting"){text, listener ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create().show()
        }

        return view
    }

}