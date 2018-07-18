package com.legocatalog.ui.main.setlist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.legocatalog.LegoCatalogApp
import com.legocatalog.R
import kotlinx.android.synthetic.main.fragment_set_list.*
import javax.inject.Inject

class SetListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var viewModel: SetListViewModel

    companion object {
        val TAB_POSITION_KEY = "tab_position_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { activity ->
            (activity.application as LegoCatalogApp).appComponent.inject(this)
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory)[SetListViewModel::class.java]
        with(viewModel) {
            message.observe(this@SetListFragment, Observer { message ->
                showMessage(message)
            })
            sets.observe(this@SetListFragment, Observer { result ->

                Log.v("------------", "${result?.size ?: -1}")

            })
            loadSets()
        }
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
    }
}