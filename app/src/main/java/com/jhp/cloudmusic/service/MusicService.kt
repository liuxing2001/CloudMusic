package com.jhp.cloudmusic.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.NotificationTarget
import com.jhp.cloudmusic.R
import com.jhp.cloudmusic.ui.MainActivity
import com.jhp.cloudmusic.ui.player.PlayerActivity
import com.jhp.cloudmusic.utils.XToastUtils
import com.jhp.cloudmusic.viewmodel.SharedViewModel
import com.lzx.starrysky.SongInfo
import com.lzx.starrysky.StarrySky

/**
 * 后台service服务
 * @author : jhp
 * @date : 2022-04-11 14:43
 */
class MusicService : Service() {
    private val shareViewModel =
        MainActivity.mainActivity?.let { ViewModelProvider(it)[SharedViewModel::class.java] }!!
    private var status = 11
    private lateinit var serviceReceiver: MyReceiver
    private var playerController = StarrySky.with()


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        println("service服务成功创建了~~~~~~~")

        //创建BroadcastReceiver
        serviceReceiver = MyReceiver()

        //创建IntentFilter
        val filter = IntentFilter()
        filter.addAction(PlayerActivity.CTL_ACTION)
        registerReceiver(serviceReceiver, filter)
    }

    private fun prepareAndPlay() {
        //添加音乐播放路径实体类、音乐信息实体类到shareViewModel中
        val musicsUrl = shareViewModel.playerSongUrl.value?.data?.get(0)
        val musicInfo = shareViewModel.getNowPlayInfo!!
        if (musicsUrl?.url != null) {
            val info = SongInfo()
            info.apply {
                songName = musicInfo.name  //音乐标题
                artist = "${musicInfo.ar[0].name} - ${musicInfo.al.name}"   //作者
                songCover = musicInfo.al.picUrl  //音乐封面
                songId = musicsUrl.id.toString()
                songUrl = musicsUrl.url.toString()
            }
            shareViewModel.nowPlayerList.add(musicInfo)
            shareViewModel.mediaPlayerList.add(info)
            shareViewModel.mediaPlayerList.let { playerController.addPlayList(it) }
            playerController.playMusicByInfo(info)
        } else {
            XToastUtils.info("暂无播放源，请切换其他歌曲")
        }
    }


    inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            println("service层接收到广播..........")
            when (intent.getIntExtra("control", 2)) {
                1 -> {
                    //上一首
                    if (playerController.isPlaying()) playerController.stopMusic()
                    playerController.skipToPrevious()
                    status = 10
                }
                2 -> {
                    //在播放状态、或者暂停状态
                    if (status == 11 || !playerController.isPlaying()) prepareAndPlay()
                    else if (status == 15) playerController.restoreMusic()
                    status = 12
                }
                3 -> {
                    //判断传入的音乐是不是正在播放的音乐 ,是则暂停不是则添加
                    shareViewModel.getNowPlayInfo?.id.toString().apply {
                        status = if (playerController.isCurrMusicIsPlayingMusic(this)) {
                            //同源则暂停
                            playerController.pauseMusic()
                            15
                        } else {
                            playerController.stopMusic()
                            prepareAndPlay()
                            12
                        }
                    }
                }
                4 -> {
                    //下一首
                    if (playerController.isPlaying()) playerController.stopMusic()
                    playerController.skipToNext()
                    status = 13
                }
            }

        }
    }
}