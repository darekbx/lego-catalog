package com.legocatalog.extensions

import android.support.v4.app.Fragment
import android.view.View

fun Fragment.safeContext() = context ?: throw IllegalStateException("Context is null")
fun View.show() { this.visibility = View.VISIBLE }
fun View.hide() { this.visibility = View.GONE }