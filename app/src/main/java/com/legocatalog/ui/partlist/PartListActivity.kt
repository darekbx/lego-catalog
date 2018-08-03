package com.legocatalog.ui.partlist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.legocatalog.LegoCatalogApp
import com.legocatalog.R
import kotlinx.android.synthetic.main.fragment_part_list.*
import javax.inject.Inject

class PartListActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var viewModel: PartsListViewModel

    lateinit var adapter: PartListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parts)
        (application as LegoCatalogApp).appComponent.inject(this)

        adapter = PartListAdapter(this, { part -> })

        initializeList()

        viewModel = ViewModelProviders.of(this, viewModelFactory)[PartsListViewModel::class.java]
        viewModel.run {
            loadParts()
            parts?.observe(this@PartListActivity, Observer {
                it?.let {
                    runOnUiThread {
                        adapter.swapData(it)
                    }
                }
            })
        }
    }

    private fun initializeList() {
        val layoutManager = LinearLayoutManager(this)
        val color = resources.getColor(R.color.colorAccent, theme)
        val divider = DividerItemDecoration(parts_list.context, layoutManager.orientation)
                .apply { setDrawable(ColorDrawable(color)) }
        parts_list.addItemDecoration(divider)
        parts_list.layoutManager = layoutManager
        parts_list.adapter = adapter
    }
}