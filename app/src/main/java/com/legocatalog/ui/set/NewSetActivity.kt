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
import com.legocatalog.LegoCatalogApp
import com.legocatalog.R
import com.legocatalog.extensions.hide
import com.legocatalog.extensions.show
import com.legocatalog.model.LegoSet
import com.legocatalog.remote.rebrickable.SetInfoWorker
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_new_set.*
import java.lang.Exception
import javax.inject.Inject

class NewSetActivity: AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var viewModel: NewSetViewModel

    var loadedSet: LegoSet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_set)
        (application as LegoCatalogApp).appComponent.inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[NewSetViewModel::class.java]
        with(viewModel) {
            viewModel.result.observe(this@NewSetActivity, Observer { result ->
                result?.let { result ->
                    when (result.first) {
                        true -> onSuccess()
                        else -> onError(result.second)
                    }
                }
            })

        }

        input.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                progress_container.show()
                viewModel.discoverSetInfo(setNumber())
                observeChanges()
            }
            return@setOnEditorActionListener true
        }
    }

    fun observeChanges() {
        viewModel.workStatus?.observe(this, Observer { workStatus ->
            workStatus?.let {
                progress_container.hide()
                when (workStatus.state) {
                    State.SUCCEEDED -> notifySuccess(workStatus.outputData)
                    State.FAILED -> notifyFailure(workStatus.outputData)
                    else -> { }
                }
            }
        })
    }

    fun onAddSetClick(v: View) {
        progress_container.show()
        loadedSet?.let { viewModel.saveSet(it) }
    }

    private fun onSuccess() {
        finish()
    }

    private fun onError(message: String) {
        Snackbar.make(input, message, Snackbar.LENGTH_LONG).show()
    }

    private fun notifySuccess(data:Data) {
        loadedSet = LegoSet.fromMap(data.keyValueMap)
        showSetInformations()
        hideKeyboard()
        hideProgress()

        add_button.isEnabled = true

        input.clearFocus()
    }

    private fun showSetInformations() {
        loadedSet?.let { legoSet ->
            with(legoSet) {
                set_number.text = number
                set_name.text = "$name ($year)"
                set_part_count.text = getString(R.string.parts_count, partsCount)
                loadImage()
            }
        }
    }

    private fun LegoSet.loadImage() {
        image_progress.show()
        Picasso.get().load(imageUrl).into(set_image, object : Callback {
            override fun onSuccess() {
                image_progress.hide()
            }

            override fun onError(e: Exception?) {
                image_progress.hide()
            }
        })
    }

    private fun notifyFailure(data: Data) {
        val message = data.getString(SetInfoWorker.ERROR_MESSAGE_KEY, null) ?: getString(R.string.unknown_error)
        Snackbar.make(input, message, Snackbar.LENGTH_LONG).show()
        hideProgress()
    }

    private fun hideProgress() {
        progress_container.hide()
    }

    private fun setNumber() = input.text.toString()

    private fun hideKeyboard() {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(input.getWindowToken(), 0)
        }
    }
}