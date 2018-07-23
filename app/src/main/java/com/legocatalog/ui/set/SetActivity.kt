package com.legocatalog.ui.set

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.legocatalog.LegoCatalogApp
import com.legocatalog.R
import com.legocatalog.extensions.hide
import com.legocatalog.extensions.show
import com.legocatalog.model.LegoSet
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_set.*
import java.lang.Exception
import javax.inject.Inject

class SetActivity : AppCompatActivity() {

    companion object {
        val SET_NUMBER = "set_number_key"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var viewModel: SetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set)
        (application as LegoCatalogApp).appComponent.inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[SetViewModel::class.java]
        with(viewModel) {
            set.observe(this@SetActivity, Observer { set ->
                set?.let {
                    fillSet(it)
                }
            })
            loadSet(setNumber)
        }
    }

    private fun fillSet(legoSet: LegoSet) {
        with(legoSet) {
            set_number.text = number
            set_part_count.text = getString(R.string.parts_count, partsCount)
            supportActionBar?.title = "$name ($year)"
            loadImage(this)
        }
    }

    private fun loadImage(legoSet: LegoSet) {
        image_progress.show()
        Picasso.get().load(legoSet.imageUrl).into(set_image, object : Callback {
            override fun onSuccess() {
                image_progress.hide()
            }

            override fun onError(e: Exception?) {
                image_progress.hide()
            }
        })
    }

    val setNumber by lazy { intent.getStringExtra(SET_NUMBER) }
}