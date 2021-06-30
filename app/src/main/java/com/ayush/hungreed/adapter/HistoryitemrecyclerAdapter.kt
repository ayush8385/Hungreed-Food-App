package com.ayush.hungreed.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ayush.hungreed.R
import com.ayush.hungreed.model.Items

class HistoryitemrecyclerAdapter(val context: Context,val itemList:ArrayList<Items>):RecyclerView.Adapter<HistoryitemrecyclerAdapter.Historyitemviewholder>() {
    class Historyitemviewholder(view: View):RecyclerView.ViewHolder(view){
        val name:TextView=view.findViewById(R.id.item_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Historyitemviewholder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.history_items_single_row,parent,false)

        return Historyitemviewholder(view)
    }

    override fun onBindViewHolder(holder: Historyitemviewholder, position: Int) {
        val dish=itemList[position]
        holder.name.text=dish.name
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}