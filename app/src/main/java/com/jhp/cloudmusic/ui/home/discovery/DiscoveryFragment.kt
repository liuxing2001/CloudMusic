package com.jhp.cloudmusic.ui.home.discovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.jhp.cloudmusic.databinding.FragmentDiscoveryBinding
import com.jhp.cloudmusic.ui.MainActivity
import com.jhp.cloudmusic.ui.common.adapter.ViewPage2Adapter
import com.jhp.cloudmusic.ui.common.adapter.ViewPageFragmentAdapter
import com.jhp.cloudmusic.ui.home.discovery.tab_layout.DiscoveryFragmentOne
import com.jhp.cloudmusic.ui.home.discovery.tab_layout.DiscoveryFragmentThree
import com.jhp.cloudmusic.ui.home.discovery.tab_layout.DiscoveryFragmentTwo

class DiscoveryFragment : Fragment() {

    private val tabsTitle = arrayOf("每日推荐", "排行榜", "热门")
    private lateinit var viewPager : ViewPager2
    private lateinit var _binding: FragmentDiscoveryBinding
    val binding: FragmentDiscoveryBinding
        get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoveryBinding.inflate(inflater, container, false)
        initPager()
        return binding.root
    }

    private fun initPager() {
        val fragments = mapOf<Int, Fragment>(
            INDEX_RECOMMEND to DiscoveryFragmentOne(),
            INDEX_RANK to DiscoveryFragmentTwo(),
            INDEX_HOT to DiscoveryFragmentThree(),
        )

        viewPager = binding.discoveryFragmentViewPage
        viewPager.adapter = ViewPageFragmentAdapter(fragments, this)
        val tabLayout = binding.discoveryFragmentTab
        TabLayoutMediator(tabLayout, binding.discoveryFragmentViewPage) { tab, position ->
            tab.text = tabsTitle[position]
        }.attach()
    }
    companion object {
        const val INDEX_RECOMMEND = 0
        const val INDEX_RANK = 1
        const val INDEX_HOT = 2
        var mainActivity: MainActivity? = null
    }
}