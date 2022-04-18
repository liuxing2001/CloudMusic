package com.jhp.cloudmusic.ui.common.mediator

import android.view.MenuItem
import androidx.core.view.forEachIndexed
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * 用于 ViewPager2 和 BottomNavigationView 之间进行绑定关联
 * @author : jhp
 * @date : 2022-04-08 16:53
 */
class BnvVp2Mediator(
    private val bnv: BottomNavigationView,
    private val vp2: ViewPager2,
    private val config: ((BottomNavigationView,ViewPager2) -> Unit)? = null
) {

    // BottomNavigationView item 与 id 的对应关系
    private val map = mutableMapOf<MenuItem,Int>()

    init {
        bnv.menu.forEachIndexed { index, item ->
            map[item] = index
        }
    }

    /**
     * 绑定
     */
    fun attach(){
        config?.invoke(bnv,vp2)
        // viewpager 页面滑动监听: 动态绑定 BottomNavigationView 的selectedItemId 属性
        vp2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                // 绑定 position 位置的 BottomNavigationView 菜单Id
                bnv.selectedItemId = bnv.menu.getItem(position).itemId
            }
        })
        // BottomNavigationView 的菜单点击事件 动态改变 viewpager 切换的页面
        bnv.setOnItemSelectedListener { item ->
            vp2.currentItem = map[item] ?: error("Bnv 的 item 的 ID ${item.itemId} 没有对应的 viewpager2 的元素")
            true
        }
    }
}