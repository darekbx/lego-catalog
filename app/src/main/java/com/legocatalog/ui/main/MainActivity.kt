package com.legocatalog.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.legocatalog.R
import com.legocatalog.ui.set.NewSetActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onAddSetClick(v: View) {
        startActivity(Intent(this, NewSetActivity::class.java))
    }
}