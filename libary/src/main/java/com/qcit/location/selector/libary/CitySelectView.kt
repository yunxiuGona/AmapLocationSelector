package com.qcit.location.selector.libary

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.qcit.location.selector.libary.adapter.CitySelectAdapter
import com.qcit.location.selector.libary.utils.ExcelUtils
import com.qcit.location.selector.libary.utils.PingYinUtil
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration
import kotlinx.android.synthetic.main.view_citys.view.*


class CitySelectView : RelativeLayout {

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    constructor(
        context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    var adapter:CitySelectAdapter?=null
    fun init() {
        ExcelUtils.readCitys(context)
        ExcelUtils.citys = PingYinUtil.getPingYins(ExcelUtils.citys)
        ExcelUtils.citys = PingYinUtil.shortPinyin(ExcelUtils.citys)
        ExcelUtils.citys

        var view = inflate(context, R.layout.view_citys, this)
        adapter = CitySelectAdapter()
        adapter?.citys=ExcelUtils.citys
        rcv_list.adapter = adapter
        rcv_list.layoutManager = LinearLayoutManager(context)
        adapter?.notifyDataSetChanged()
    }
}