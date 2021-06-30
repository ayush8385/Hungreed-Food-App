package com.ayush.hungreed.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.ayush.hungreed.R
import com.ayush.hungreed.adapter.FavoriterecyclerAdapter
import com.ayush.hungreed.database.FoodDatabase
import com.ayush.hungreed.database.FoodEntity

class FavoriteFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var recyclerAdapter: FavoriterecyclerAdapter
    lateinit var layoutManager:RecyclerView.LayoutManager
    var dbFoodList= listOf<FoodEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_favorite, container, false)

        recyclerView=view.findViewById(R.id.recyclerfav)
        progressLayout=view.findViewById(R.id.progresslayout)
        progressBar=view.findViewById(R.id.progressbar)
        layoutManager= LinearLayoutManager(activity)

        progressLayout.visibility=View.GONE

        dbFoodList=Retrieverestuarants(activity as Context).execute().get()

        if(activity!=null){
            recyclerAdapter=FavoriterecyclerAdapter(activity as Context,dbFoodList)
            recyclerView.adapter=recyclerAdapter
            recyclerView.layoutManager=layoutManager
        }

        return view
    }

    class Retrieverestuarants(val context: Context):AsyncTask<Void,Void,List<FoodEntity>>(){
        override fun doInBackground(vararg params: Void?): List<FoodEntity> {
            val db = Room.databaseBuilder(context,FoodDatabase::class.java,"food-db").build()
            return db.foodDao().getAllRestaurants()
        }

    }

}