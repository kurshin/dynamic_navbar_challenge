package com.cinemo.test.presentation.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.cinemo.test.domain.Item
import com.cinemo.test.presentation.ui.main.page.PageFragment

class SectionsPagerAdapter(
    private val context: Context,
    fm: FragmentManager,
    private var items: List<Item>
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return PageFragment.newInstance(items[position])
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return items[position].title
    }

    override fun getCount(): Int {
        return items.size
    }

    fun updateItems(newItems: List<Item>) {
        items = newItems
        notifyDataSetChanged()
    }
}