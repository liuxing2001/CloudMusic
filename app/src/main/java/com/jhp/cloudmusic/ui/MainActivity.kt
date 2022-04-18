package com.jhp.cloudmusic.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.jhp.cloudmusic.databinding.ActivityMainBinding
import com.jhp.cloudmusic.ui.common.adapter.MainViewPagerAdapter
import com.jhp.cloudmusic.ui.common.mediator.BnvVp2Mediator
import com.jhp.cloudmusic.ui.home.dashboard.DashboardFragment
import com.jhp.cloudmusic.ui.home.home.HomeFragment
import com.jhp.cloudmusic.ui.home.notifications.NotificationsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        initBinding()
        initListener()
        initPager()
        initView()
    }
    private fun initBinding(){
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initListener() {
        binding.includeMain.toolbar.apply {
            setNavigationOnClickListener {
                openDraw()
            }
        }
    }

    @SuppressLint("RtlHardcoded")
    private fun openDraw() {
        val drawerLayout = binding.drawerLayout
        drawerLayout.openDrawer(Gravity.LEFT)
    }

    //初始化Drawer
    private fun initView() {

    }

    //初始化viewpager
    private fun initPager() {
        val fragments = mapOf<Int, Fragment>(
            INDEX_HOME to HomeFragment(),
            INDEX_DASHBOARD to DashboardFragment(),
            INDEX_NOTIFICATIONS to NotificationsFragment(),
        )
        binding.apply {
            includeMain.vp2Main.adapter = MainViewPagerAdapter(this@MainActivity,fragments)

            BnvVp2Mediator(includeMain.bnvMain,includeMain.vp2Main){ _, vp2 ->
                vp2.isUserInputEnabled = true// 设置是否响应用户滑动
            }.attach()
        }
    }



    companion object{
        const val INDEX_HOME = 0
        const val INDEX_DASHBOARD = 1
        const val INDEX_NOTIFICATIONS = 2
    }
}