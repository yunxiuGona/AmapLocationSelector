高德地图地址选择器
AMAP location selectoe

# AmapLocationSelector
高德地图控制台注册应用，且获取APPKey
请参照：https://developer.amap.com/api/android-sdk/guide/create-project/get-key


1、Main module's gradle file.
```
implementation 'com.qcit.mapselector:libary:0.4'
```

2、In main module's AndroidManifest.xml File,add the AMAP's APPKey in <Application> tag.
```
<application....>
          <meta-data
            android:name="com.amap.api.v2.apikey"
            android:value="高德APPKey"/>
</application>     
```
            
 3、In layout's view.
 ```
 <com.qcit.location.selector.libary.LocationSelectView
        android:id="@+id/selectView"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
    </com.qcit.location.selector.libary.LocationSelectView>
```
 4、In main activity or fragment , In function:onCreate()
 ```
 selectView.create(savedInstanceState);
 ```
 
 5、Get the final selected address information
 ```
 selectView.getSelectedLocation()
 ```
6、In your main activity or fragment
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
