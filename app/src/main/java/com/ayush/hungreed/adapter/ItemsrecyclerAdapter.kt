package com.ayush.hungreed.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ayush.hungreed.R
import com.ayush.hungreed.model.Dish

class ItemsrecyclerAdapter(val context: Context, var itemList:ArrayList<Dish>,var orderList:ArrayList<Dish>):RecyclerView.Adapter<ItemsrecyclerAdapter.ItemsviewHolder>() {

    class ItemsviewHolder(view: View):RecyclerView.ViewHolder(view){
        val item_add: Button =view.findViewById(R.id.item_add)
        val dishName:TextView=view.findViewById(R.id.item_name)
        val dishPrice:TextView=view.findViewById(R.id.item_rate)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsviewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_single_row,parent,false)
        return ItemsviewHolder(view)

    }

    override fun onBindViewHolder(holder: ItemsviewHolder, position: Int) {
        val dishes=itemList[position]
        holder.dishName.text=dishes.foodName
        holder.dishPrice.text=dishes.foodPrice

        holder.item_add.setOnClickListener {

            if(dishes in orderList){
                orderList.remove(dishes)
                holder.item_add.text = "Add"
                holder.item_add.setBackgroundColor(Color.parseColor("#D8EAAF34"))
            }
            else{
                orderList.add(dishes)
                holder.item_add.text = "Remove"
                holder.item_add.setBackgroundColor(Color.parseColor("#FFFFAB00"))
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}