package com.qcit.location.selector.libary.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.qcit.location.selector.libary.OnCityClickedListener
import com.qcit.location.selector.libary.R
import com.qcit.location.selector.libary.models.City
import java.util.*

class CitySelectAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var letters = listOf<String>(
        "A",
        "B",
        "C",
        "D",
        "E",
        "F",
        "G",
        "H",
        "J",
        "K",
        "L",
        "M",
        "N",
        "P",
        "Q",
        "R",
        "S",
        "T",
        "W",
        "X",
        "Y",
        "Z"
    )
    var onCityClickedListener: OnCityClickedListener? = null
        set(value) {
            field = value
        }
        get() = field
    var citys = mutableListOf<City>()
        set(value) {
            field = processData(value)
        }
        get() = field

    var TYPE_HEADER = 1;
    var TYPE_NORMAL = 2;


    fun processData(citys: MutableList<City>): MutableList<City> {
        var datas = mutableListOf<City>()
        for (i in 0..letters.size - 1) {
            datas.add(City(letters.get(i), "", ""))
            datas.addAll(getCitysInLetter(letters.get(i), citys))
        }
        return datas
    }

    fun getCitysInLetter(letter: String, citys: MutableList<City>): List<City> {
        var data = mutableListOf<City>()
        var group = letter.toLowerCase()
        for (i in citys) {
            if (i.belongLetter.equals(group)) {
                data.add(i)
            }
        }
        return data;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> HeaderHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.city_select_header, parent, false)
            )
            else -> ItemHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.city_select_item, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderHolder) {
            holder.tvTitle.text = getItem(position).name
        }
        if (holder is ItemHolder) {
            holder.tvTitle.text = getItem(position).name
            holder.itemView.setOnClickListener {
                onCityClickedListener?.OnCityClicked(
                    getItem(
                        position
                    )
                )
            }
        }
    }

    fun getItem(position: Int): City {
        return citys[position]
    }

    override fun getItemCount(): Int {
        return citys.size
    }

    override fun getItemViewType(position: Int): Int {
        var city = citys.get(position);
        if (city.adcode.equals(""))
            return TYPE_HEADER
        else
            return TYPE_NORMAL
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    }

    class HeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    }
}
