package com.legocatalog.ui.main.setlist

import android.content.Context
import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.legocatalog.databinding.AdapterSetBinding
import com.legocatalog.ui.model.SetInfo
import com.squareup.picasso.Picasso

@BindingAdapter("app:imageUri")
fun setImageUrl(view: ImageView, url:String?) {
    Picasso.get().load(url).into(view)
}

class SetListAdapter(val context: Context, val onItemClick: (set: SetInfo) -> Unit)
    : RecyclerView.Adapter<SetListAdapter.ViewHolder>() {

    var setInfos = emptyList<SetInfo>()

    fun swapData(setInfos: List<SetInfo>) {
        this.setInfos = setInfos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterSetBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = setInfos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val set = setInfos.get(position)
        holder.bind(set, onItemClick)
    }

    val inflater by lazy { LayoutInflater.from(context) }

    class ViewHolder(val binding: AdapterSetBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(setInfo: SetInfo, onItemClick: (set: SetInfo) -> Unit) {
            with(binding) {
                set = setInfo
                root.setOnClickListener { onItemClick(setInfo) }
                executePendingBindings()
            }
        }
    }
}