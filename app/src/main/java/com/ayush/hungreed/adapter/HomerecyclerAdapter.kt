package com.ayush.hungreed.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ayush.hungreed.R
import com.ayush.hungreed.activity.FoodItems
import com.ayush.hungreed.database.FoodEntity
import com.ayush.hungreed.model.Resturants
import com.squareup.picasso.Picasso


class HomerecyclerAdapter(val context: Context,val itemList:ArrayList<Resturants>) :RecyclerView.Adapter<HomerecyclerAdapter.HomeViewHolder>(){
    class HomeViewHolder(view: View):RecyclerView.ViewHolder(view){
        val resturantImage:ImageView=view.findViewById(R.id.resturant_img)
        val resturantName:TextView=view.findViewById(R.id.resturant_name)
        val resturantRating:TextView=view.findViewById(R.id.resturant_rating)
        val resturantPrice:TextView=view.findViewById(R.id.resturant_rate)
        val resturantFav:ImageView=view.findViewById(R.id.resturant_fav)
        val parLayout:LinearLayout=view.findViewById(R.id.parentlayout)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.home_single_row,parent,false)

        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val food=itemList[position]
        holder.resturantName.text=food.name
        holder.resturantRating.text=food.rating
        holder.resturantPrice.text=food.cost_for_one
        Picasso.get().load(food.image_url).into(holder.resturantImage)

        holder.parLayout.setOnClickListener {
            context.startActivity(Intent(context, FoodItems::class.java).putExtra("id",food.id).putExtra("name",food.name))
        }

       val foodEntity = FoodEntity(
            restaurantId=food.id,
            restaurantName = food.name,
            restaurantPrice = food.cost_for_one,
            restaurantRating = food.rating,
            restaurantImage = food.image_url
           )

        val checkFav=FoodItems.DBAsyncTask(context,foodEntity,1).execute()

        val fav=checkFav.get()
        if(fav){
            holder.resturantFav.setImageResource(R.drawable.ic_lmyfav_foreground)
        }
        else{
            holder.resturantFav.setImageResource(R.drawable.ic_favorite_foreground)
        }
        holder.resturantFav.setOnClickListener {
            if(!FoodItems.DBAsyncTask(context,foodEntity,1).execute().get()){
                val result=FoodItems.DBAsyncTask(context,foodEntity,2).execute().get()
                if(result){
                    Toast.makeText(context, "${holder.resturantName.text} added to favorite", Toast.LENGTH_SHORT).show()
                    holder.resturantFav.setImageResource(R.drawable.ic_lmyfav_foreground)
                }
                else{
                    Toast.makeText(context,"Some Error Occurred",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                val result=FoodItems.DBAsyncTask(context,foodEntity,3).execute().get()
                if(result){
                    Toast.makeText(context, "${holder.resturantName.text} removed from favorite", Toast.LENGTH_SHORT).show()
                    holder.resturantFav.setImageResource(R.drawable.ic_favorite_foreground)
                }
                else{
                    Toast.makeText(context, "Some Error Occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
}