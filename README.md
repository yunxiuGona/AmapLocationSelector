高德地图地址选择器
AMAP location selector

# AmapLocationSelector
高德地图控制台注册应用，并获取APPKey<br>
请参照：<a href="https://developer.amap.com/api/android-sdk/guide/create-project/get-key">高德地图SDK申请Key</a>

# AmapLocationSelector使用方法
1、主Module的 gradle 文件中.
```
implementation 'com.qcit.mapselector:libary:0.5'
```

2、主Module的 AndroidManifest.xml 文件中，<Application>标签中添加高德地图APPKey  
```
<application....>
          <meta-data
            android:name="com.amap.api.v2.apikey"
            android:value="高德APPKey"/>
</application>     
```
            
 3、Layout布局文件中
 ```
 <com.qcit.location.selector.libary.LocationSelectView
        android:id="@+id/selectView"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
    </com.qcit.location.selector.libary.LocationSelectView>
```
 4、在对应的activity 或者 Fragment 的 onCreate() 函数中
 ```
 selectView.create(savedInstanceState);
 ```
 
 5、获取最终的定位信息(地址名、经纬度等)
 ```
 selectView.getSelectedLocation()
 ```
6、在对应的activity 或者 Fragment(用于返回按钮关闭城市选择等窗口)
 ```
 override fun onBackPressed() {
        if(!selectorView.onBack()){
            super.onBackPressed()
        }
    }
 ```

![](https://github.com/15563988825/AmapLocationSelector/blob/master/screenShot/device-2021-03-08-154929.png)
![](https://github.com/15563988825/AmapLocationSelector/blob/master/screenShot/device-2021-03-08-154949.png)
![](https://github.com/15563988825/AmapLocationSelector/blob/master/screenShot/device-2021-03-08-155018.png)
