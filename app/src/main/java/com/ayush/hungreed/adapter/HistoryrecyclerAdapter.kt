package com.ayush.hungreed.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayush.hungreed.R
import com.ayush.hungreed.model.Orders


class HistoryrecyclerAdapter(var context: Context, var itemList:ArrayList<Orders>):RecyclerView.Adapter<HistoryrecyclerAdapter.HistoryViewholder>(){

    class HistoryViewholder(view: View):RecyclerView.ViewHolder(view){

        val historyname:TextView=view.findViewById(R.id.history_name)
        val historydate:TextView=view.findViewById(R.id.history_date)
        val historyamount:TextView=view.findViewById(R.id.history_amount)
        val recyclerView:RecyclerView=view.findViewById(R.id.recycler_items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewholder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.history_single_row,parent,false)

        return HistoryViewholder(view)

    }

    override fun onBindViewHolder(holder: HistoryViewholder, position: Int) {
        val items=itemList[position]
        holder.historyname.text=items.restaurant_name
        holder.historydate.text=items.order_placed_at
        holder.historyamount.text="\u20B9"+items.total_cost

        var recyclerAdapter = HistoryitemrecyclerAdapter(context,items.food_items)
        val layoutManager = LinearLayoutManager(context)

        holder.recyclerView.adapter = recyclerAdapter
        holder.recyclerView.layoutManager = layoutManager


    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}