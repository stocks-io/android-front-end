package com.stocks.stocks_io.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.stocks.stocks_io.POJO.Results
import com.stocks.stocks_io.R
import io.realm.RealmResults

class PopupSearchFragmentAdapter(private var dataSet: RealmResults<Results>) : RecyclerView.Adapter<PopupSearchFragmentAdapter.ViewHolder>() {

    var searchClickListener: SearchClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.autocomplete_row, parent, false))
    }

    override fun getItemCount(): Int {
        return if (dataSet.size > 5) 5 else dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.searchItem?.text = dataSet[position]?.Symbol
        holder?.searchItem?.setOnClickListener {
            searchClickListener?.onItemClicked(holder.searchItem?.text.toString())
        }
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var searchItem: TextView? = null

        init {
            searchItem = itemView?.findViewById(R.id.search_item)
        }
    }

    fun updateSearchResults(newData: RealmResults<Results>) {
        dataSet = newData
        notifyDataSetChanged()
    }
}

interface SearchClickListener {
    fun onItemClicked(item: String)
}