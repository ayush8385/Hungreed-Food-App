package com.ayush.hungreed.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ayush.hungreed.R
import com.ayush.hungreed.activity.FoodItems
import com.ayush.hungreed.database.FoodEntity
import com.squareup.picasso.Picasso

class FavoriterecyclerAdapter(val context: Context,val itemList:List<FoodEntity>):RecyclerView.Adapter<FavoriterecyclerAdapter.FavoriteviewHolder>() {
    class FavoriteviewHolder(view: View):RecyclerView.ViewHolder(view){
        val parentlayout:CardView=view.findViewById(R.id.home_row)
        val fav_image:ImageView=view.findViewById(R.id.fav_img)
        val fav_name:TextView=view.findViewById(R.id.fav_name)
        val fav_rate:TextView=view.findViewById(R.id.fav_rate)
        val fav_rating:TextView=view.findViewById(R.id.fav_rating)
        val fav_fav:ImageView=view.findViewById(R.id.fav_fav)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteviewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.favorite_single_row,parent,false)
        return FavoriteviewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteviewHolder, position: Int) {
        val foods=itemList[position]
        holder.fav_name.text=foods.restaurantName
        holder.fav_rate.text=foods.restaurantPrice
        holder.fav_rating.text=foods.restaurantRating
        Picasso.get().load(foods.restaurantImage).into(holder.fav_image)

        holder.parentlayout.setOnClickListener {
            context.startActivity(Intent(context, FoodItems::class.java).putExtra("id",foods.restaurantId).putExtra("name",foods.restaurantName))
        }

        val foodEntity = FoodEntity(
            restaurantId=foods.restaurantId,
            restaurantName = foods.restaurantName,
            restaurantPrice = foods.restaurantPrice,
            restaurantRating = foods.restaurantRating,
            restaurantImage = foods.restaurantImage
        )

        val checkFav=FoodItems.DBAsyncTask(context,foodEntity,1).execute()
        val fav=checkFav.get()
        if(fav){
            holder.fav_fav.setImageResource(R.drawable.ic_lmyfav_foreground)
        }
        else{
            holder.fav_fav.setImageResource(R.drawable.ic_favorite_foreground)
        }
        holder.fav_fav.setOnClickListener {
            if(!FoodItems.DBAsyncTask(context,foodEntity,1).execute().get()){
                val result=FoodItems.DBAsyncTask(context,foodEntity,2).execute().get()
                if(result){
                    Toast.makeText(context, "${holder.fav_name.text} added to favorite", Toast.LENGTH_SHORT).show()
                    holder.fav_fav.setImageResource(R.drawable.ic_lmyfav_foreground)
                }
                else{
                    Toast.makeText(context,"Some Error Occurred", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                val result=FoodItems.DBAsyncTask(context,foodEntity,3).execute().get()
                if(result){
                    Toast.makeText(context, "${holder.fav_name.text} removed from favorite", Toast.LENGTH_SHORT).show()
                    holder.fav_fav.setImageResource(R.drawable.ic_favorite_foreground)
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