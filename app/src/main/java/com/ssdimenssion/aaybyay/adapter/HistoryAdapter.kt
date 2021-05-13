package com.ssdimenssion.aaybyay.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ssdimenssion.aaybyay.R
import com.ssdimenssion.aaybyay.database.Txn

class HistoryAdapter(val context: Context,val itemdel:OnDeleteItem, val txns:List<Txn>):RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(view: View):RecyclerView.ViewHolder(view){

        val date = view.findViewById<TextView>(R.id.date)
        val purpose = view.findViewById<TextView>(R.id.purpose)
        val amt = view.findViewById<TextView>(R.id.amt)
        val delete = view.findViewById<ImageView>(R.id.delete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.single_record,parent,false)

        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.date.text ="Dated:- ${txns[position].date}"
        holder.purpose.text = txns[position].purpose
        holder.amt.text ="Amount:- ₹ ${txns[position].Amount.toString()}"
        holder.delete.setOnClickListener{
            itemdel.deleteItem(txns[position])

        }

    }

    override fun getItemCount(): Int {
        return txns.size
    }

    interface OnDeleteItem{
        fun deleteItem(data:Txn)

    }
}