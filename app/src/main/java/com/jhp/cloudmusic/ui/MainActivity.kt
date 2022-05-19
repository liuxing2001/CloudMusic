package com.jhp.cloudmusic.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.jhp.cloudmusic.R
import com.jhp.cloudmusic.dao.UserInfoDao
import com.jhp.cloudmusic.databinding.ActivityMainBinding
import com.jhp.cloudmusic.model.NowPlayInfo
import com.jhp.cloudmusic.service.MusicService
import com.jhp.cloudmusic.ui.common.adapter.ViewPage2Adapter
import com.jhp.cloudmusic.ui.common.mediator.BnvVp2Mediator
import com.jhp.cloudmusic.ui.home.attention.AttentionFragment
import com.jhp.cloudmusic.ui.home.discovery.DiscoveryFragment
import com.jhp.cloudmusic.ui.home.mine.MineFragment
import com.jhp.cloudmusic.ui.home.podcast.PodcastFragment

import com.jhp.cloudmusic.ui.home.village.VillageFragment
import com.jhp.cloudmusic.ui.login.LoginActivity
import com.jhp.cloudmusic.ui.player.PlayerActivity
import com.jhp.cloudmusic.ui.search.SearchActivity
import com.jhp.cloudmusic.utils.XToastUtils
import com.jhp.cloudmusic.viewmodel.SharedViewModel
import com.lzx.starrysky.StarrySky
import com.lzx.starrysky.utils.contextReflex
import com.xiaoyouProject.searchbox.SearchFragment
import com.xiaoyouProject.searchbox.custom.IOnSearchClickListener
import com.xiaoyouProject.searchbox.entity.CustomLink


class MainActivity : AppCompatActivity() {
    private var userInfo = UserInfoDao.getUserInfo()
    private lateinit var binding: ActivityMainBinding
    private lateinit var searchFragment: SearchFragment<Any>
    private val viewModel by lazy {
        mainActivity.let {  ViewModelProvider(this)[SharedViewModel::class.java] }
    }

    val data: MutableList<CustomLink<Any>> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initView()
        initListener()
        initObserver()
        initPager()
        initBar()
    }


    private fun initObserver() {
        //搜索
        viewModel.keyWordLiveData.observe(this, Observer {
            data.clear()
            it.getOrNull()?.forEach { it ->
                data.add(CustomLink(it.name, it))
            }
            // 这里我们设置关键词预测显示的内容
            searchFragment.setLinks(data)
        })
    }

    private fun initBar() {
        window.statusBarColor = Color.WHITE
    }


    private fun initBinding() {
        mainActivity = this
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initListener() {
        binding.includeMain.toolbar.apply {
            setNavigationOnClickListener {
                openDraw()
            }
        }
        binding.includeMain.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_search -> {
                    searchFragment.showFragment(supportFragmentManager, SearchFragment.TAG)
                }
            }
            false
        }
        binding.includeMain.tvSearch.setOnClickListener{

            searchFragment.showFragment(supportFragmentManager, SearchFragment.TAG);
        }
        searchFragment.setOnSearchClickListener(object : IOnSearchClickListener<Any> {
            /**
             * 点击搜索按钮时触发
             * @param keyword 搜索的关键词
             */
            override fun onSearchClick(keyword: String) {
                viewModel.searchKeyword(keyword)
            }

            /**
             * 点击关键词预测链接时触发
             * @param data 链接携带的数据
             */
            override fun onLinkClick(data: Any?) {
                val gson = Gson()
                val obj = gson.fromJson(gson.toJson(data), NowPlayInfo::class.java)
                viewModel.setNowPlayerSong(obj)
                viewModel.setPlayerSongId(obj.id.toString())
                viewModel.playerSongUrlLiveData.observe(searchFragment) {
                    val item = it.getOrNull()
                    if (item != null) {
                        viewModel.playerSongUrl.value = item
                    }
                }
                val intent = Intent(searchFragment.context, PlayerActivity::class.java)
                startActivity(intent)
            }

            /**
             * 当搜索框内容改变时触发
             * @param keyword 搜索的关键词
             */
            override fun onTextChange(keyword: String) {
                viewModel.searchKeyword(keyword)
            }
        })
    }

    @SuppressLint("RtlHardcoded")
    private fun openDraw() {
        val drawerLayout = binding.drawerLayout
        drawerLayout.openDrawer(Gravity.LEFT)
    }

    //初始化Drawer
    @RequiresApi(Build.VERSION_CODES.R)
     fun initView() {
        //搜索框
        searchFragment = SearchFragment.newInstance()
        val navigationView = binding.navView
        val headerLayout = navigationView.getHeaderView(0)
        val headImg = headerLayout.findViewById(R.id.nav_header_img) as ImageView
        val name = headerLayout.findViewById(R.id.nav_header_username) as TextView
        val sign = headerLayout.findViewById(R.id.nav_header_sign) as TextView
        name.text = userInfo.profile.nickname
        sign.text = userInfo.profile.signature
        Glide.with(this).load(userInfo.profile.avatarUrl).into(headImg)
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
//                R.id.nav_setting -> {
//                    val intent = Intent(this, SettingActivity::class.java)
//                    startActivity(intent)
//                }
                R.id.nav_logoOut -> {
                    try {
                        UserInfoDao.logout()
                        val intent = Intent(
                            this,
                            LoginActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    } catch (e: Exception) {
                        XToastUtils.error("退出失败");
                    }
                }
                else -> XToastUtils.error("该功能尚未开发");
            }
            false
        }
    }

    //初始化viewpager
    private fun initPager() {
        val fragments = mapOf<Int, Fragment>(
            INDEX_DISCOVERY to DiscoveryFragment(),
            INDEX_PODCAST to PodcastFragment(),
            INDEX_MINE to MineFragment(),
            INDEX_ATTENTION to AttentionFragment(),
            INDEX_VILLAGE to VillageFragment()
        )
        binding.apply {
            includeMain.vp2Main.requestDisallowInterceptTouchEvent(false)
            includeMain.vp2Main.adapter = ViewPage2Adapter(this@MainActivity, fragments)

            BnvVp2Mediator(includeMain.bnvMain, includeMain.vp2Main) { _, vp2 ->
                vp2.isUserInputEnabled = true// 设置是否响应用户滑动
            }.attach()
        }
    }


    companion object {
        const val INDEX_DISCOVERY = 0
        const val INDEX_PODCAST = 1
        const val INDEX_MINE = 2
        const val INDEX_ATTENTION = 3
        const val INDEX_VILLAGE = 4
        var mainActivity: MainActivity? = null
    }
}