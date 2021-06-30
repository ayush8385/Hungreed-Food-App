package com.ayush.hungreed.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ayush.hungreed.R
import com.ayush.hungreed.model.Dish

class CartrecyclerAdapter(val context: Context,val itemList:ArrayList<Dish>):RecyclerView.Adapter<CartrecyclerAdapter.CartviewHolder>() {
    class CartviewHolder(view: View):RecyclerView.ViewHolder(view){
        val cartName:TextView=view.findViewById(R.id.cart_name)
        val cartRate:TextView=view.findViewById(R.id.cart_rate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartviewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.cart_single_row,parent,false)

        return CartviewHolder(view)
    }

    override fun onBindViewHolder(holder: CartviewHolder, position: Int) {
        val item=itemList[position]
        holder.cartName.text=item.foodName
        holder.cartRate.text=item.foodPrice
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}