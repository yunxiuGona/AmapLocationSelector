package com.qcit.location.selector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        selectorView.create(savedInstanceState);
        selectorView.getSelectedLocation()
    }

    override fun onBackPressed() {
        if(selectorView.onBack()){}else{
            super.onBackPressed()
        }
    }
}