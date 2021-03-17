package com.qcit.location.selector.libary

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.model.CameraPosition
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.LatLngBounds
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.qcit.location.selector.libary.adapter.LocationSearchAdapter
import com.qcit.location.selector.libary.models.City
import com.qcit.location.selector.libary.utils.KeybordUtil
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.view_selector.view.*
import java.util.*


class LocationSelectView : RelativeLayout, LocationSearchAdapter.OnItemClickedListener,
        OnCityClickedListener {
    private var activity: Activity? = null
    private var amap: AMap? = null
    private var adapter: LocationSearchAdapter? = null

    var listenForChanges = true
    var location: Location? = null

    var citycode: String? = null
    var cityname: String? = null

    fun create(savedInstanceState: Bundle?) {
        mapview.onCreate(savedInstanceState)
        amap = mapview.map
        amap?.moveCamera(CameraUpdateFactory.zoomTo(18f))
        amap?.isMyLocationEnabled = true
        amap?.uiSettings?.isMyLocationButtonEnabled = false
        amap?.uiSettings?.isZoomControlsEnabled = false
        amap?.uiSettings?.isScaleControlsEnabled = false
        amap?.setOnMapTouchListener { showAroundList(false) }
        amap?.setOnCameraChangeListener(object : AMap.OnCameraChangeListener {
            override fun onCameraChange(p0: CameraPosition?) {}
            override fun onCameraChangeFinish(position: CameraPosition?) {
                var latlon = LatLng(position?.target?.latitude!!, position?.target?.longitude!!)
                doAroundSearch(latlon)
            }
        })
    }


    public fun getSelectedLocation(): Location? {
        return location
    }

    fun init() {
        var view = inflate(context, R.layout.view_selector, this)
        activity = getActivityFromView()
        aroundList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = LocationSearchAdapter()
        adapter?.onItemClickedListener = this
        aroundList.adapter = adapter
        requestLocationPermission()
        editSearch.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                showAroundList(false)
        }
        editSearch.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    showAroundList(false)
                    doQuerySearchInCity(editSearch.text.toString())
                }
                return false
            }
        })
        editSearch.doAfterTextChanged {
            if (listenForChanges) doQuerySearchInCity(editSearch.text.toString())
            showAroundList(false)
        }
        rl_bottom.setOnClickListener {
            if (isArounfListShowing()) {
                showAroundList(false)
            } else showAroundList(true)
        }
        rlMineLocation.setOnClickListener {
            cameraTo(
                    LatLng(
                            amap?.myLocation?.latitude!!,
                            amap?.myLocation?.longitude!!
                    )
            )
        }
        citySelectView.visibility = View.GONE
        citySelectView?.onCityClickedListener = this
        rl_city.setOnClickListener {
            if (citySelectView.visibility == View.GONE) citySelectView.visibility = View.VISIBLE
        }
    }

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

    fun doAroundSearch(latlon: LatLng) {
        var query = PoiSearch.Query("", "", "")
        query.cityLimit = true
        query.pageSize = 200
        query.pageNum = 0
        var poiSearch = PoiSearch(context, query)
        poiSearch.setOnPoiSearchListener(object : PoiSearch.OnPoiSearchListener {
            override fun onPoiSearched(poiResult: PoiResult?, resultCode: Int) {
                if (resultCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (poiResult != null && poiResult.getQuery() != null) {
                        if (poiResult.query == query) {
                            var poiItems = poiResult.pois
                            if (poiItems != null && poiItems.size > 0) {
                                tv_city.setText(poiItems.get(0).cityName)
                                cityname = poiItems.get(0).cityName
                                citycode = poiItems.get(0).cityCode
                            }
                            adapter?.data = poiItems
                            adapter?.notifyDataSetChanged()
                            if (adapter?.hasData() == true) {
                                showAroundList(true)
                            }
                            aroundList.smoothScrollToPosition(0)
                        }
                    }
                }
            }

            override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {}
        })
        poiSearch.setBound(
                PoiSearch.SearchBound(
                        LatLonPoint(latlon.latitude, latlon.longitude),
                        1000,
                        true
                )
        )
        poiSearch.searchPOIAsyn()
    }


    fun doQuerySearchInCity(key: String) {
        var query = PoiSearch.Query(key, "", tv_city.text.toString())
        query.pageSize = 100
        var poiSearch = PoiSearch(context, query)
        poiSearch.setOnPoiSearchListener(object : PoiSearch.OnPoiSearchListener {
            override fun onPoiSearched(result: PoiResult?, p1: Int) {
                if (p1 == AMapException.CODE_AMAP_SUCCESS) {
                    if (result != null && result.query != null) {
                        val poiItems: List<PoiItem> = result.pois
                        showListPop(poiItems)
                    }
                } else {
                }
            }
            override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {
            }
        })
        poiSearch.searchPOIAsyn()
    }

    val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, emptyList())

    @SuppressLint("NewApi")
    fun showListPop(lst: List<PoiItem>) {
        var array = lst.map {
            it.title
        }
        editSearch.threshold = 1;
        editSearch.setAdapter(arrayAdapter)
        arrayAdapter.clear()
        arrayAdapter.addAll(array)
        arrayAdapter.filter.filter(null)
        arrayAdapter.notifyDataSetChanged()

        editSearch.setOnItemClickListener { parent, view, position, id ->
            var item = lst.get(position)
            listenForChanges = false;
            editSearch.setText(item.title, TextView.BufferType.EDITABLE)
            listenForChanges = true;
            cameraTo(LatLng(item.latLonPoint.latitude, item.latLonPoint.longitude))
            doAroundSearch(LatLng(item.latLonPoint.latitude, item.latLonPoint.longitude))
        }
    }

    fun getActivityFromView(): Activity? {
        var context: Context? = this.context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }

    fun requestLocationPermission() {
        AndPermission.with(context)
                .runtime()
                .permission(Permission.Group.LOCATION)
                .onGranted {
                    startLocalMe()
                }
                .onDenied {
                    Toast.makeText(activity, "获取权限失败", Toast.LENGTH_SHORT).show()
                }
                .start()
    }

    private fun startLocalMe() {
        var mLocationClient = AMapLocationClient(context)
        var option = AMapLocationClientOption()
        option.isOnceLocation = true
        mLocationClient.setLocationListener {
            tv_city.setText(it.city)
            cityname = it.city
            citycode = it.cityCode

            var location: Location = Location()
            location.latitude = it.latitude
            location.longitude = it.longitude
            location.poiName = it.poiName
            location.address = it.address
            location.adCode = it.adCode
            location.province = it.province
            location.city = it.city
            location.cityCode = it.cityCode
            location.aoiName = it.aoiName
            location.country = it.country
            location.description = it.description
            location.street = it.street
            location.streetNum = it.streetNum
            location.locationDetail = it.locationDetail
            location.buildingId = it.buildingId
            location.time = it.time
            cameraTo(LatLng(it.latitude, it.longitude))
        }
    }

    fun cameraTo(latlng: LatLng) {
        val b = LatLngBounds.builder()
        b.include(latlng)
        amap?.animateCamera(CameraUpdateFactory.newLatLngBounds(b.build(), 100))
    }

    var around_show = false
    fun showAroundList(show: Boolean) {
        if (show) {
            if (around_show) return
            around_show = true
            aroundList.visibility = View.VISIBLE
            ivExport.setImageResource(R.drawable.ic_arrow_down)
            KeybordUtil.closeKeybord(activity)
        }
        if (!show) {
            if (!around_show) return
            around_show = false
            aroundList.visibility = View.GONE
            ivExport.setImageResource(R.drawable.ic_arrow_up)
        }
    }

    fun isArounfListShowing(): Boolean {
        return around_show
    }

    override fun OnItemClicked(position: Int, poiItem: PoiItem?) {
        var lat = poiItem?.latLonPoint?.latitude
        var lon = poiItem?.latLonPoint?.longitude
        var latlon = LatLng(lat!!, lon!!);

        poiItem.let {
            var location: Location = Location()
            location.latitude = it?.latLonPoint!!.latitude
            location.longitude = it.latLonPoint!!.longitude
            location.poiName = it.title
            location.address = it.snippet
            location.adCode = it.adCode
            location.province = it.provinceName
            location.city = it.cityName
            location.cityCode = it.cityCode
            location.aoiName = it.adName
            location.country = ""
            location.description = ""
            location.street = ""
            location.streetNum = ""
            location.locationDetail = ""
            location.buildingId = ""
            location.time = 0L
        }

        cameraTo(latlon)
        doAroundSearch(latlon)
    }

    fun onBack(): Boolean {
        if (citySelectView.visibility == View.VISIBLE) {
            citySelectView.visibility = View.GONE
            return true
        }
        return false
    }

    override fun OnCityClicked(city: City) {
        if (citySelectView.visibility == View.VISIBLE) {
            citySelectView.visibility = View.GONE
        }
        tv_city.text = city.name
    }
}