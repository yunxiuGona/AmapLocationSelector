高德地图地址选择器

# AmapLocationSelector
高德地图控制台注册应用，且获取APPKey
请参照：https://developer.amap.com/api/android-sdk/guide/create-project/get-key



1、主项目的Geadle文件中
```
implementation 'com.qcit.mapselector:libary:0.1'
```

2、
主项目的AndroidManifest.xml文件<Application>节点中添加高德地图APPKey
```
<application....>
          <meta-data
            android:name="com.amap.api.v2.apikey"
            android:value="高德APPKey"/>
```
            
 3、
 layout中View
 ```
 <com.qcit.location.selector.libary.LocationSelectorView
        android:id="@+id/selectorView"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
    </com.qcit.location.selector.libary.LocationSelectorView>
```
 4、
 Activity或者Fragment的onCreate函数中
 ```
 selectorView.create(savedInstanceState);
 ```
 
 5、获取选中的地址信息
 ```
 selectorView.getSelectedLocation()
 ```
