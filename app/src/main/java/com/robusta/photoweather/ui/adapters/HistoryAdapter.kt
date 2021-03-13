package com.robusta.photoweather.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.robusta.photoweather.R
import com.robusta.photoweather.data.HistoryItem

class HistoryAdapter(private var  historyList: List<HistoryItem>, val clickListener: (HistoryItem) -> Unit) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {

        return HistoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false))
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position],position, clickListener)
    }

    fun getHistory(adapterPosition: Int): HistoryItem {
        return historyList[adapterPosition]
    }

    fun loadItems(newItems: List<HistoryItem>) {
        historyList = newItems
    }


    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(history: HistoryItem, position: Int, clickListener: (HistoryItem) -> Unit) {
            itemView.findViewById<ImageView>(R.id.item_thumbnail).setImageBitmap(history.thumbnail)
            itemView.findViewById<TextView>(R.id.item_date).setText(history.date)
            val cardItem = itemView as CardView
            itemView.setOnClickListener { clickListener(history)}
        }
    }

}