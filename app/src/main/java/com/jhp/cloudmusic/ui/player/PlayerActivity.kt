package com.jhp.cloudmusic.ui.player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

import com.jhp.cloudmusic.databinding.ActivityPlayerBinding
import com.jhp.cloudmusic.extension.toMinAndSeconds
import com.jhp.cloudmusic.service.MusicService
import com.jhp.cloudmusic.ui.MainActivity
import com.jhp.cloudmusic.utils.StatusBarUtils
import com.jhp.cloudmusic.viewmodel.SharedViewModel
import com.lzx.starrysky.StarrySky
import java.util.*
import kotlin.concurrent.schedule


class PlayerActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityPlayerBinding


    //    音乐广播接收者
    private lateinit var musicBroadcastReceiver: MusicBroadcastReceiver

    private val viewModel by lazy {
        ViewModelProvider(this)[PlayerActivityViewModel::class.java]
    }
    private val shareViewModel by lazy {
        MainActivity.mainActivity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }!!
    }
    val binding: ActivityPlayerBinding
        get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initBroadcastReceiver()
        initListener()
        initBar()
    }

    private fun initListener() {
        val intent = Intent(CTL_ACTION)
//        shareViewModel.playerSongUrlLiveData.observe(this) {
//            val item = it.getOrNull()
//            if (item != null) {
//                shareViewModel.playerSongUrl.value = item
//                createPlay(intent)
//            }
//        }


        Timer().schedule(1000) {
      //      println("playersongurl是~~~~： " + shareViewModel.playerSongUrl)

           createPlay(intent)
        }

    }

    private fun initBar() {
        StatusBarUtils.setStatusBarDarkMode(this)
    }

    private fun initBinding() {
        _binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        StatusBarUtils.translucent(this, Color.TRANSPARENT)
    }

    private fun createPlay(intent: Intent) {
        val status = shareViewModel.getPlayStatus
        if (status == 11 || status == 15) {
            //没有播放或暂停时去播放
            intent.putExtra("control", 2)
        } else if (status == 12) {
            //正在播放
            intent.putExtra("control", 3)
        }
        sendBroadcast(intent)
    }

    private fun initBroadcastReceiver() {
        // 创建广播过滤器，指定只接收android.net.conn.CONNECTIVITY_CHANGE的广播事件
        val intentFilter = IntentFilter()
        //指定BroadcastReceiver监听的Action
        intentFilter.addAction(UPDATE_ACTION)
        musicBroadcastReceiver = MusicBroadcastReceiver()
        //注册BroadcastReceiver
        registerReceiver(musicBroadcastReceiver, intentFilter)
    }

    //自定义的BroadcastReceiver，负责监听接收从Service传回的广播
    inner class MusicBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            //获取Intent中的update消息，update代表播放状态
            val update = intent.getIntExtra("update", -1)
            binding.musicLength.text = StarrySky.with().getDuration().toMinAndSeconds()
            //设置音乐媒体时长
            if (update == 10 || update == 13) {
                val index = StarrySky.with().getNowPlayingIndex()
                val obj = shareViewModel.nowPlayerList[index]
                shareViewModel.setNowPlayerSong(obj)
                shareViewModel.changeStatus(12)
            } else if (update == 12) {
                //  startRotateAlways()
                shareViewModel.changeStatus(12)
            } else if (update == 15) {
                //  pauseRotateAlways()
                shareViewModel.changeStatus(15)
            }
        }
    }

    companion object {
        const val CTL_ACTION = "com.jhp.cloudmusic.MUSIC_BROADCAST"
        const val UPDATE_ACTION = "com.jhp.cloudmusic.UPDATE_ACTION"

        // 动画循环时长
        private const val DURATION_CD = 32_000L
        private const val ANIMATION_REPEAT_COUNTS = -1
        private const val ANIMATION_PROPERTY_NAME = "rotation"
    }
}