package com.legocatalog.ui.main

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.legocatalog.LegoCatalogApp
import com.legocatalog.R
import com.legocatalog.ui.set.NewSetActivity
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as LegoCatalogApp).appComponent.inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[MainViewModel::class.java]
        with(viewModel) { }
    }

    fun onAddSetClick(v: View) {
        startActivity(Intent(this, NewSetActivity::class.java))
    }

}