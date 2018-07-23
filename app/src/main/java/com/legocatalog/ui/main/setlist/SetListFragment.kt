package com.legocatalog.ui.main.setlist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.legocatalog.LegoCatalogApp
import com.legocatalog.R
import com.legocatalog.model.LegoSet
import com.legocatalog.ui.main.MainActivity
import com.legocatalog.ui.set.SetActivity
import kotlinx.android.synthetic.main.fragment_set_list.*
import javax.inject.Inject

class SetListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var viewModel: SetListViewModel

    lateinit var setListAdapter: SetListAdapter

    companion object {
        val TAB_POSITION_KEY = "tab_position_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { activity ->
            (activity.application as LegoCatalogApp).appComponent.inject(this)
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory)[SetListViewModel::class.java]
    }

    private fun showMessage(message: String?) {
        message?.let { message ->
            Snackbar.make(sets_list, message, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_set_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeList(view)
        observeViewModel()
        viewModel.loadSets(LegoSet.Theme.values()[tabPosition])
    }

    private fun observeViewModel() {
        with(viewModel) {
            message.observe(this@SetListFragment, Observer { message ->
                showMessage(message)
            })
            sets.observe(this@SetListFragment, Observer { result ->
                result?.let {
                    setListAdapter.swapData(result)
                    displaySummary(result)
                    updateTabTitle(result)
                }
            })
        }
    }

    private fun displaySummary(result: List<LegoSet>) {
        var partsCount = result.sumBy { set -> set.partsCount.toInt() }
        summary.text = getString(R.string.summary, partsCount)
    }

    private fun updateTabTitle(result: List<LegoSet>) {
        activity?.let {
            if (it is MainActivity) {
                it.updateTitle(tabPosition, result.size)
            }
        }
    }

    private fun initializeList(view: View) {
        setListAdapter = SetListAdapter(view.context, { clickedSet ->
            openSet(clickedSet)
        })
        val layoutManager = LinearLayoutManager(view.context)
        val color = resources.getColor(R.color.colorAccent, activity?.theme)
        val divider = DividerItemDecoration(sets_list.context, layoutManager.orientation)
                .apply { setDrawable(ColorDrawable(color)) }
        sets_list.addItemDecoration(divider)
        sets_list.layoutManager = layoutManager
        sets_list.adapter = setListAdapter
    }

    private fun openSet(legoSet: LegoSet) {
        startActivity(
                Intent(context, SetActivity::class.java).apply {
                    putExtra(SetActivity.SET_NUMBER, legoSet.number)
                }
        )
    }

    val tabPosition by lazy { arguments?.getInt(TAB_POSITION_KEY) ?: 0 }
}