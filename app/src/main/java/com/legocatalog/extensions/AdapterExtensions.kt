package com.legocatalog.extensions

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso

@BindingAdapter("app:imageUri")
fun setImageUrl(view: ImageView, url:String?) {
    Picasso.get().load(url).into(view)
}
