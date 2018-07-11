package com.legocatalog.ui.set

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.work.Data
import androidx.work.State
import androidx.work.WorkManager
import com.legocatalog.LegoCatalogApp
import com.legocatalog.R
import com.legocatalog.remote.rebrickable.SetInfoWorker
import kotlinx.android.synthetic.main.activity_new_set.*
import java.util.*
import javax.inject.Inject

class NewSetActivity: AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var viewModel: NewSetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_set)
        (application as LegoCatalogApp).appComponent.inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[NewSetViewModel::class.java]
        with(viewModel) {


        }

        input.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                progress_container.visibility = View.VISIBLE
                val workId = viewModel.discoverSetInfo(setNumber())
                observeChanges(workId)
            }
            return@setOnEditorActionListener true
        }
    }

    fun observeChanges(uuid: UUID) {
        WorkManager.getInstance()?.let {
            it.getStatusById(uuid).observe(this, Observer { workStatus ->
                workStatus?.let {
                    when (workStatus.state) {
                        State.SUCCEEDED -> notifySuccess(workStatus.outputData)
                        State.FAILED -> notifyFailure(workStatus.outputData)
                        else -> { }
                    }
                }
            })
        }
    }

    fun notifySuccess(data:Data) {
        val set = com.legocatalog.model.Set.fromMap(data.keyValueMap)



        progress_container.visibility = View.GONE
    }

    fun notifyFailure(data: Data) {
        val message = data.getString(SetInfoWorker.ERROR_MESSAGE_KEY, null) ?: getString(R.string.unknown_error)
        Snackbar.make(input, message, Snackbar.LENGTH_LONG).show()

        progress_container.visibility = View.GONE
    }

    fun setNumber() = input.text.toString()
}