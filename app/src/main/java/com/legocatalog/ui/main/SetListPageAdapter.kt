package com.legocatalog.ui.main

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.legocatalog.R
import com.legocatalog.ui.main.setlist.SetListFragment

class SetListPageAdapter(
        val context: Context,
        fragmentManager: FragmentManager?) : FragmentStatePagerAdapter(fragmentManager) {

    companion object {
        val TAB_COUNT = 3
    }

    val tabCounters = mutableMapOf<Int, Int>()

    fun setCounter(tabIntex: Int, count: Int) {
        tabCounters.put(tabIntex, count)
    }

    override fun getItem(position: Int) =
            SetListFragment().apply {
                arguments = createArguments(position)
            }

    private fun createArguments(position: Int) =
            Bundle(1).apply {
                putInt(SetListFragment.TAB_POSITION_KEY, position)
            }

    override fun getCount() = TAB_COUNT

    override fun getPageTitle(position: Int) : String {
        var title = resources.getStringArray(R.array.types)[position]
        if (tabCounters.containsKey(position)) {
            title = "$title (${tabCounters[position]})"
        }
        return title
    }

    private val resources by lazy { context.resources }
}