package com.legocatalog.ui.partlist

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.legocatalog.LegoCatalogApp
import com.legocatalog.R
import com.legocatalog.ui.filters.FiltersActivity
import kotlinx.android.synthetic.main.fragment_part_list.*
import javax.inject.Inject

class PartListActivity : AppCompatActivity() {

    companion object {
        val FILTERS_REQUEST_CODE = 1
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var viewModel: PartsListViewModel

    lateinit var adapter: PartListAdapter
    var activeFilters: Bundle? = null

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

    fun applyFilters() {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_parts, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.run {
            when (itemId) {
                R.id.menu_filter -> openFilters()
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == FILTERS_REQUEST_CODE && resultCode == Activity.RESULT_OK -> {
                data?.run {
                    activeFilters = extras
                    applyFilters()
                }
            }
        }
    }

    fun openFilters() {
        val intent = Intent(this, FiltersActivity::class.java)
        if (activeFilters != null) {
            intent.putExtras(activeFilters)
        }
        startActivityForResult(intent, FILTERS_REQUEST_CODE)
    }
}