package com.jhp.cloudmusic.ui.common.adapter

import androidx.fragment.app.Fragment

import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPageFragmentAdapter(private val fragments: Map<Int, Fragment>, fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment =
        fragments[position] ?: error("请确保fragments数据源和 viewpager2的index匹配设置")

}