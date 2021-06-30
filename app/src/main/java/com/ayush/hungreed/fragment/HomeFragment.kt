package com.ayush.hungreed.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ayush.hungreed.R
import com.ayush.hungreed.adapter.HomerecyclerAdapter
import com.ayush.hungreed.model.Resturants
import com.ayush.hungreed.util.ConnectionManager

class HomeFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: HomerecyclerAdapter
    lateinit var layoutManager:RecyclerView.LayoutManager
    lateinit var progressLayout: RelativeLayout
    var resturantlist= arrayListOf<Resturants>()
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView=view.findViewById(R.id.recyclerhome)
        progressLayout=view.findViewById(R.id.progresslayout)
        progressBar=view.findViewById(R.id.progressbar)

        progressLayout.visibility=View.VISIBLE

        layoutManager=LinearLayoutManager(activity)

        val url="http://13.235.250.119/v2/restaurants/fetch_result/"
        val queue=Volley.newRequestQueue(context)

        if(ConnectionManager().checkconnectivity(activity as Context)){
            val jsonObjectRequest=object :JsonObjectRequest(Method.GET,url,null,Response.Listener {
                 try{
                     progressLayout.visibility=View.GONE
                     val res=it.getJSONObject("data")
                     val success=res.getBoolean("success")
                     if(success){
                         val data=res.getJSONArray("data")
                         for(i in 0 until data.length()){
                             val foodJSONObject=data.getJSONObject(i)
                             val foodObject=Resturants(
                                 foodJSONObject.getString("id"),
                                 foodJSONObject.getString("name"),
                                 foodJSONObject.getString("rating"),
                                 foodJSONObject.getString("cost_for_one"),
                                 foodJSONObject.getString("image_url")
                             )
                             resturantlist.add(foodObject)
                             recyclerAdapter= HomerecyclerAdapter(activity as Context,resturantlist)
                             recyclerView.adapter=recyclerAdapter
                             recyclerView.layoutManager=layoutManager
                         }
                     }
                     else{
                         Toast.makeText(context,"Error occurred",Toast.LENGTH_SHORT).show()
                     }
                 }
                 catch (e:Exception){
                     Toast.makeText(context,"Some Unexpected Error occurred",Toast.LENGTH_SHORT).show()
                 }
            },Response.ErrorListener {
                Toast.makeText(context,"Volley Error occurred",Toast.LENGTH_SHORT).show()
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
            val dialog= AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet is Not Connected")
            dialog.setPositiveButton("Open Setting"){text,listener->
                val setting= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(setting)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){text,listener->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        return view
    }

}