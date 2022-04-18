package com.jhp.cloudmusic.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import androidx.annotation.RequiresApi
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

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        initBinding()
        initListener()
        initPager()
        initView()
    }

    private fun initBinding() {
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
    @RequiresApi(Build.VERSION_CODES.R)
    private fun initView() {
        //初始化状态栏字体为黑色，背景为白色
        //在API级别30中 setSystemUiVisibility() 已弃用.
        // 因此，您应该使用 WindowInsetsController .
        // 以下代码将使状态栏变亮，这意味着状态栏中的文本将为黑色
        //https://www.it1352.com/2287672.html
        val window = window
        val decorView = window.decorView

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val wic = decorView.windowInsetsController
            wic!!.setSystemBarsAppearance(
                APPEARANCE_LIGHT_STATUS_BARS,
                APPEARANCE_LIGHT_STATUS_BARS
            )
        } else
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        window.statusBarColor = Color.TRANSPARENT
    }

    //初始化viewpager
    private fun initPager() {
        val fragments = mapOf<Int, Fragment>(
            INDEX_HOME to HomeFragment(),
            INDEX_DASHBOARD to DashboardFragment(),
            INDEX_NOTIFICATIONS to NotificationsFragment(),
        )
        binding.apply {
            includeMain.vp2Main.adapter = MainViewPagerAdapter(this@MainActivity, fragments)

            BnvVp2Mediator(includeMain.bnvMain, includeMain.vp2Main) { _, vp2 ->
                vp2.isUserInputEnabled = true// 设置是否响应用户滑动
            }.attach()
        }
    }


    companion object {
        const val INDEX_HOME = 0
        const val INDEX_DASHBOARD = 1
        const val INDEX_NOTIFICATIONS = 2
    }
}