package com.legocatalog.ui.partlist

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.legocatalog.LegoCatalogApp
import com.legocatalog.R
import javax.inject.Inject

class PartListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var viewModel: PartsListViewModel

    companion object {
        val SET_NUMBER = "set_number_key"
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

        Toast.makeText(context, setNumber, Toast.LENGTH_SHORT).show()
    }

    val setNumber by lazy { arguments?.getString(PartListFragment.SET_NUMBER) }
}