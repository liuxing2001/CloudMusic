package com.jhp.cloudmusic.service

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.NotificationTarget
import com.jhp.cloudmusic.NotificationReceiver
import com.jhp.cloudmusic.R
import com.jhp.cloudmusic.ui.MainActivity
import com.jhp.cloudmusic.ui.player.PlayerActivity
import com.jhp.cloudmusic.utils.XToastUtils
import com.jhp.cloudmusic.ui.SharedViewModel
import com.lzx.starrysky.SongInfo
import com.lzx.starrysky.StarrySky

/**
 * 后台service服务
 * @author : jhp
 * @date : 2022-04-11 14:43
 */
class MusicService : Service() {
    private val shareViewModel =
        MainActivity.mainActivity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }!!

    //定义音乐播放状态，10代表上一首,11代表没有播放，12代表正在播放，13代表下一首，15表示暂停
    private var status = 11
    private lateinit var serviceReceiver: MyReceiver
    private var playerController = StarrySky.with()

    private val channelId: String = "音频通道id"
    private var notification: Notification? = null
    private lateinit var contentView: RemoteViews
    private var notificationTarget: NotificationTarget? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        //创建BroadcastReceiver
        serviceReceiver = MyReceiver()
        //创建IntentFilter
        val filter = IntentFilter();
        filter.addAction(PlayerActivity.CTL_ACTION);
        registerReceiver(serviceReceiver, filter);
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
            XToastUtils.info("暂无播放源，请切换其他歌曲");
        }
    }

    inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val control: Int = intent.getIntExtra("control", 2)
            when (control) {
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
                        if (playerController.isCurrMusicIsPlayingMusic(this)) {
                            //同源则暂停
                            playerController.pauseMusic()
                            status = 15
                        } else {
                            playerController.stopMusic()
                            prepareAndPlay()
                            status = 12
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
            //初始化通知栏
            initNotificationBar()
            // 1.上下文 2.图标控件的ResID 2.RemoteView  ,4 Notification 5 Notification_ID
            notificationTarget = NotificationTarget(
                context,
                R.id.bgmMusicImageView,
                contentView,
                notification,
                R.string.app_name
            );
            //设置通知栏封面
            if (context != null) {
                Glide.with(context).asBitmap()
                    .load(playerController.getNowPlayingSongInfo()?.songCover)
                    .placeholder(R.drawable.ic_logo_app)
                    .into(notificationTarget!!)
            }
            val sendIntent = Intent(PlayerActivity.UPDATE_ACTION)
            sendIntent.putExtra("update", status)
            //发送广播，将被PlayerActivity组件中的BroadcastReceiver接收到
            sendBroadcast(sendIntent)
        }
    }


    /*
    * 建立自定义远程视图RemoteView
    * */
    private fun initRemoteView() {
        val musicInfo = playerController.getNowPlayingSongInfo()!!
        contentView = RemoteViews(packageName, R.layout.big_notification) // 远程视图
        contentView.apply {
            setTextViewText(R.id.musicTitleTextView, musicInfo.songName)
            setTextViewText(
                R.id.musicSubTitleTextView,
                musicInfo.artist
            )
        }
        if (playerController.isPlaying()) {
            contentView.setImageViewResource(R.id.stopImageView, R.drawable.icon_play_pause)
        } else {
            contentView.setImageViewResource(R.id.stopImageView, R.drawable.icon_music_play)
        }

        // 实现中止/播放
        val intentStop = Intent("stop")
        intentStop.putExtra("control",2)
        val pIntentStop = PendingIntent.getBroadcast(this, 0, intentStop, 0)
        contentView.setOnClickPendingIntent(R.id.stopImageView, pIntentStop)

        //下一首事件
        val intentNext = Intent("next") //发送播放下一曲的通知
        val pIntentNext = PendingIntent.getBroadcast(this, 0, intentNext, 0)
        contentView.setOnClickPendingIntent(R.id.nextImageView, pIntentNext)
        //上一首事件
        val intentLast = Intent("last") //发送播放上一曲的通知
        val pIntentLast = PendingIntent.getBroadcast(this, 0, intentLast, 0)
        contentView.setOnClickPendingIntent(R.id.lastImageView, pIntentLast)
        // 关闭通知栏
        val intentCancelled = Intent("notification_cancelled")
        val pIntentCancelled = PendingIntent.getBroadcast(this, 0, intentCancelled, 0)
        contentView.setOnClickPendingIntent(R.id.audio_close_btn, pIntentCancelled)
    }


    /*
    * 建立通知栏
    * */
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun initNotificationBar() {
        initRemoteView()
        val manager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notification = Notification()
        val intent = Intent(this, NotificationReceiver::class.java) // 建立一个跳转到活动页面的意图
        val pendingIntent = PendingIntent.getActivity(
            this,
            R.string.app_name,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        var builder: Notification.Builder? = null // 建立一个通知消息的构造器

        //  判断是不是android 8以上的版本，由于从8.0版本后唤起通知栏必需要填写渠道id和渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val description = "有趣,有用,有将来"
            val importance: Int = NotificationManager.IMPORTANCE_HIGH //重要性级别
            // getPackageName() 获取APP渠道名称
            val mChannel = NotificationChannel(channelId, "音频", importance)
            mChannel.description = description //渠道描述
            manager.createNotificationChannel(mChannel) //建立通知渠道
            mChannel.setSound(null, null)
            builder = Notification.Builder(this, channelId)
            builder.setContentIntent(pendingIntent)
                .setCustomContentView(contentView)
                .setCustomBigContentView(contentView)
                .setShowWhen(false)
                .setSmallIcon(R.drawable.ic_like) // 必填
                .build()
            notification = builder.build()
        }
//        notification!!.flags = notification.FLAG_NO_CLEAR //设置通知点击或滑动时不被清除*/
        manager.notify(R.string.app_name, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(serviceReceiver)
    }
}