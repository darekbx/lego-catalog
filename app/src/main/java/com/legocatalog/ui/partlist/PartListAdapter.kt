package com.legocatalog.ui.partlist

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import com.legocatalog.databinding.AdapterPartBinding
import com.legocatalog.ui.filters.FiltersActivity
import com.legocatalog.ui.model.Part

class PartListAdapter(val context: Context, val onItemClick: (part: Part) -> Unit)
    : RecyclerView.Adapter<PartListAdapter.ViewHolder>() {

    var parts = mutableListOf<Part>()
    var partsInitial = emptyList<Part>()

    fun swapData(parts: MutableList<Part>) {
        this.parts = parts
        this.partsInitial = ArrayList(parts)
        notifyDataSetChanged()
    }

    fun filter(filterDefinition: Bundle) {
        parts.clear()
        partsInitial.forEach { part ->
            var conditionsMet: Boolean? = false

            val quantityFrom = checkQuantityFrom(filterDefinition, FiltersActivity.QUANTITY_FROM_KEY, { from -> part.quantity > from })
            val quantityTo = checkQuantityFrom(filterDefinition, FiltersActivity.QUANTITY_TO_KEY, { to -> part.quantity < to })

            val name = checkValue(filterDefinition, FiltersActivity.NAME_KEY, { part.name })
            val color = checkValue(filterDefinition, FiltersActivity.COLOR_KEY, { part.colorName })
            val elementId = checkValue(filterDefinition, FiltersActivity.ELEMENT_ID_KEY, { part.elementId })

            if ((quantityFrom == null || quantityFrom)
                    && (quantityTo == null || quantityTo)
                    && (name == null || name)
                    && (color == null || color)
                    && (elementId == null || elementId)) {
                parts.add(part)
            }

            // TODO: by theme
        }

        notifyDataSetChanged()
    }

    private fun checkValue(filterDefinition: Bundle, key: String, partValue: () -> String?): Boolean? {
        val name = filterDefinition.getString(key)
        if (!TextUtils.isEmpty(name)) {
            return partValue.invoke() == name
        }
        return null
    }

    private fun checkQuantityFrom(filterDefinition: Bundle, key: String, condition: (value: Int) -> Boolean): Boolean? {
        if (filterDefinition.containsKey(key)) {
            val value = filterDefinition.getInt(key)
            return condition.invoke(value)
        }
        return null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterPartBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = parts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val part = parts.get(position)
        holder.bind(part, onItemClick)
    }

    val inflater by lazy { LayoutInflater.from(context) }

    class ViewHolder(val binding: AdapterPartBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(part: Part, onItemClick: (part: Part) -> Unit) {
            binding.part = part
            with(binding) {
                root.setOnClickListener { onItemClick(part) }
                executePendingBindings()
            }
        }
    }
}