package com.legocatalog.ui.partlist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.legocatalog.LegoCatalogApp
import com.legocatalog.R
import kotlinx.android.synthetic.main.fragment_part_list.*
import javax.inject.Inject

class PartListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var viewModel: PartsListViewModel

    lateinit var adapter: PartListAdapter

    companion object {
        val SET_ID = "set_id_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { activity ->
            (activity.application as LegoCatalogApp).appComponent.inject(this)
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory)[PartsListViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_part_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PartListAdapter(view.context, { part -> })

        initializeList(view)

        setNumber?.let { setNumber ->
            with(viewModel) {
                loadParts(setNumber)
                parts?.observe(this@PartListFragment, Observer {
                    it?.let {
                        view.post {
                            adapter.swapData(it)
                        }
                    }
                })
            }
        }
    }

    private fun initializeList(view: View) {
        val layoutManager = LinearLayoutManager(view.context)
        val color = resources.getColor(R.color.colorAccent, activity?.theme)
        val divider = DividerItemDecoration(parts_list.context, layoutManager.orientation)
                .apply { setDrawable(ColorDrawable(color)) }
        parts_list.addItemDecoration(divider)
        parts_list.layoutManager = layoutManager
        parts_list.adapter = adapter
    }

    val setNumber by lazy { arguments?.getLong(PartListFragment.SET_ID) }
}