package com.qcit.location.selector.libary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amap.api.services.core.PoiItem
import com.qcit.location.selector.libary.R

class LocationSearchAdapter : RecyclerView.Adapter<LocationSearchAdapter.InnerHolder>() {

    var data: List<PoiItem>? = null
        set(value) {
            field = value
        }
        get() = field

    var onItemClickedListener: OnItemClickedListener? = null
        set(value) {
            field = value
        }
        get() = field

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        var v =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_locations, parent, false)
        return InnerHolder(v)
    }

    public fun hasData():Boolean{
        if(data!=null&& data?.size!! >0) return true
        return false
    }
    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.tv_title.text = getItem(position)?.title
        holder.tv_content.text = getItem(position)?.snippet
        holder.itemView.setOnClickListener {
            onItemClickedListener?.OnItemClicked(position, getItem(position))
        }
    }

    fun getItem(position: Int): PoiItem? {
        return data?.get(position)
    }

    override fun getItemCount(): Int {
        return if (data != null) data?.size!! else 0
    }

    class InnerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_title: TextView = itemView.findViewById(R.id.tv_title);
        var tv_content: TextView = itemView.findViewById(R.id.tv_content);
    }


    interface OnItemClickedListener {
        public fun OnItemClicked(position: Int, poiItem: PoiItem?);
    }
}