package com.legocatalog.ui.partlist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.legocatalog.databinding.AdapterPartBinding
import com.legocatalog.ui.model.Part

class PartListAdapter(val context: Context, val onItemClick: (part: Part) -> Unit)
    : RecyclerView.Adapter<PartListAdapter.ViewHolder>() {

    var parts = emptyList<Part>()

    fun swapData(parts: List<Part>) {
        this.parts = parts
        notifyDataSetChanged()
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