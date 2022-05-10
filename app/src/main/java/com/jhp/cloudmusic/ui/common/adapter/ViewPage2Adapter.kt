package com.jhp.cloudmusic.ui.common.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jhp.cloudmusic.ui.MainActivity
import com.jhp.cloudmusic.ui.home.discovery.DiscoveryFragment

/**
 * viewpager2适配器类
 * @author : jhp
 * @date : 2022-04-08 16:47
 */
class ViewPage2Adapter(fa: MainActivity, private val fragments: Map<Int, Fragment>) :
    FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment =
        fragments[position] ?: error("请确保fragments数据源和 viewpager2的index匹配设置")

}