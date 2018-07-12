package com.legocatalog.ui.set

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.work.Data
import androidx.work.State
import androidx.work.WorkManager
import com.legocatalog.LegoCatalogApp
import com.legocatalog.R
import com.legocatalog.model.LegoSet
import com.legocatalog.remote.rebrickable.SetInfoWorker
import com.squareup.picasso.Picasso
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

    fun onAddSetClick(v: View) {

        finish()
    }

    private fun notifySuccess(data:Data) {
        val legoSet = LegoSet.fromMap(data.keyValueMap)
        showSetInformations(legoSet)
        hideKeyboard()
        hideProgress()

        add_button.isEnabled = true

        input.clearFocus()
    }

    private fun showSetInformations(legoSet: LegoSet) {
        with(legoSet) {
            set_number.text = number
            set_name.text = "$name ($year)"
            set_part_count.text = getString(R.string.parts_count, partsCount)
            Picasso.get().load(imageUrl).into(set_image)
        }
    }

    private fun notifyFailure(data: Data) {
        val message = data.getString(SetInfoWorker.ERROR_MESSAGE_KEY, null) ?: getString(R.string.unknown_error)
        Snackbar.make(input, message, Snackbar.LENGTH_LONG).show()
        hideProgress()
    }

    private fun hideProgress() {
        progress_container.visibility = View.GONE
    }

    private fun setNumber() = input.text.toString()

    private fun hideKeyboard() {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(input.getWindowToken(), 0)
        }
    }
}