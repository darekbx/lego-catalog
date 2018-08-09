package com.legocatalog.ui.filters

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import com.legocatalog.LegoCatalogApp
import com.legocatalog.R
import com.legocatalog.ui.model.SetInfo
import kotlinx.android.synthetic.main.activity_filters.*
import javax.inject.Inject

class FiltersActivity : AppCompatActivity() {

    companion object {
        val THEME_KEY = "theme_key"
        val NAME_KEY = "name_key"
        val ELEMENT_ID_KEY = "element_id_key"
        val COLOR_KEY = "color_key"
        val QUANTITY_FROM_KEY = "quantity_from_key"
        val QUANTITY_TO_KEY = "quantity_to_key"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    internal lateinit var viewModel: FiltersViewModel
    lateinit var typeAdapter: ArrayAdapter<String>
    lateinit var namesAdapter: ArrayAdapter<String>
    lateinit var elementsAdapter: ArrayAdapter<String>
    lateinit var colorsAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filters)
        (application as LegoCatalogApp).appComponent.inject(this)

        initializeAutompletes();
        initializeTypes()

        viewModel = ViewModelProviders.of(this, viewModelFactory)[FiltersViewModel::class.java]
        with(viewModel) {
            loadFilters()
            names.observe(this@FiltersActivity, Observer { namesAdapter.addAll(it) })
            elementIds.observe(this@FiltersActivity, Observer { elementsAdapter.addAll(it) })
            colors.observe(this@FiltersActivity, Observer { colorsAdapter.addAll(it) })
        }
    }

    fun onApplyClick(view: View) {
        val filtersBundle = createFiltersBundle()
        setResult(Activity.RESULT_OK, Intent().apply { putExtras(filtersBundle) })
        finish()
    }

    private fun createFiltersBundle(): Bundle {
        val theme = selectedTheme()
        val name = filter_name.text.toString()
        val elementId = filter_element_id.text.toString()
        val color = filter_color.text.toString()
        val quantityFrom = quantinty_from.text.toString()
        val quantityTo = quantinty_to.text.toString()

        return with(Bundle(8)) {

            if (theme != null) {
                putString(THEME_KEY, theme.name)
            }

            putIfNotEmpty(NAME_KEY, name)
            putIfNotEmpty(ELEMENT_ID_KEY, elementId)
            putIfNotEmpty(COLOR_KEY, color)

            putAndConvertToInt(QUANTITY_FROM_KEY, quantityFrom)
            putAndConvertToInt(QUANTITY_TO_KEY, quantityTo)

            return this
        }
    }

    private fun Bundle.putIfNotEmpty(key: String, value: String) {
        if (TextUtils.isEmpty(value)) return
        putString(key, value)
    }

    private fun Bundle.putAndConvertToInt(key: String, value: String) {
        if (!TextUtils.isEmpty(value)) {
            val toValue = value.toIntOrNull()
            if (toValue != null) {
                putInt(key, toValue)
            }
        }
    }

    private fun selectedTheme(): SetInfo.Theme? {
        val selectedPosition = filter_type.selectedItemPosition
        if (selectedPosition == 0) {
            return null
        }
        return SetInfo.Theme.values()[selectedPosition - 1]
    }

    private fun initializeAutompletes() {
        ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item).run {
            namesAdapter = this
            filter_name.setAdapter(this)
        }

        ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item).run {
            elementsAdapter = this
            filter_element_id.setAdapter(this)
        }

        ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item).run {
            colorsAdapter = this
            filter_color.setAdapter(this)
        }
    }

    private fun initializeTypes() {
        val items = mutableListOf(getString(R.string.filter_all_types)).apply {
            addAll(resources.getStringArray(R.array.types))
        }
        typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        filter_type.adapter = typeAdapter
    }
}