package com.jhp.cloudmusic.ui.player

import android.animation.ObjectAnimator
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.dirror.lyricviewx.OnPlayClickListener
import com.dirror.lyricviewx.OnSingleClickListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jhp.cloudmusic.R

import com.jhp.cloudmusic.databinding.ActivityPlayerBinding
import com.jhp.cloudmusic.extension.goneAlphaAnimation
import com.jhp.cloudmusic.extension.toMinAndSeconds
import com.jhp.cloudmusic.extension.visibleAlphaAnimation
import com.jhp.cloudmusic.service.MusicService
import com.jhp.cloudmusic.ui.MainActivity
import com.jhp.cloudmusic.ui.common.adapter.KotlinDataAdapter
import com.jhp.cloudmusic.utils.StatusBarUtils
import com.jhp.cloudmusic.viewmodel.SharedViewModel
import com.lzx.starrysky.OnPlayProgressListener
import com.lzx.starrysky.SongInfo
import com.lzx.starrysky.StarrySky
import com.xuexiang.xui.utils.WidgetUtils
import jp.wasabeef.glide.transformations.BlurTransformation
import java.util.*
import kotlin.concurrent.schedule


class PlayerActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityPlayerBinding
    private lateinit var adapter: KotlinDataAdapter<SongInfo>
    private lateinit var bottomView: View
    private lateinit var dialog: BottomSheetDialog
    private var params: String? =null

    //    音乐广播接收者
    private lateinit var musicBroadcastReceiver: MusicBroadcastReceiver

    private val viewModel by lazy {
        ViewModelProvider(this)[PlayerActivityViewModel::class.java]
    }
    private val shareViewModel by lazy {
        MainActivity.mainActivity?.let { ViewModelProvider(it)[SharedViewModel::class.java] }!!
    }
    val binding: ActivityPlayerBinding
        get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initData()
        initView()
        initBroadcastReceiver()
        initListener()
        initObserver()
        initBar()

    }

    private fun initBar() {
        StatusBarUtils.setStatusBarDarkMode(this)
    }

    private fun initBinding() {

        _binding = ActivityPlayerBinding.inflate(layoutInflater)
        params = intent.getStringExtra("MusicTab")
        setContentView(binding.root)
    }

     private fun initData() {

        //  加载底部弹出层
        bottomView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog_list, null)
        // 弹出层列表 初始化adapter、layout manager、
        val recyclerView: RecyclerView = bottomView.findViewById(R.id.bottom_sheet_RecyclerView)
        val layoutManager = LinearLayoutManager(this)
        adapter = KotlinDataAdapter.Builder<SongInfo>()
            .setLayoutId(R.layout.layout_song_item)
            .setData(shareViewModel.mediaPlayerList as ArrayList<SongInfo>)
            .addBindView { itemView, itemData, index ->
                val songIndex : TextView = itemView.findViewById(R.id.songIndex)
                val songTitle: TextView = itemView.findViewById(R.id.songTitle)
                val songSubTitle: TextView = itemView.findViewById(R.id.songSubTitle)
                songTitle.text = itemData.songName
                songSubTitle.text = itemData.artist
                songIndex.text = "${index+1}"
                itemView.setOnClickListener {
                    //播放音乐
                    val intent: Intent = Intent(CTL_ACTION)
                    intent.putExtra("control", 2);
                    sendBroadcast(intent);
                    dialog.hide()
                }
            }
            .create()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        bottomView.findViewById<TextView>(R.id.bottom_sheet_title).text = "播放列表"
        dialog = BottomSheetDialog(this@PlayerActivity)
        dialog.setContentView(bottomView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }

     private fun initView() {


        // 绑定歌词控件的样式
        binding.lyricViewX.apply {
            setLabel(shareViewModel.getNowPlayInfo!!.name)
            //设置非当前行歌词字体颜色 [normalColor]
            setNormalColor(com.xuexiang.xui.R.color.xui_btn_green_normal_color)
            //设置拖动歌词时时间线的颜色
            setTimelineColor(com.xuexiang.xui.R.color.xui_config_color_white)
            //设置拖动歌词时右侧时间字体颜色
            setTimeTextColor(com.xuexiang.xui.R.color.xui_config_color_white)
            //设置拖动歌词时选中歌词的字体颜色
            setTimelineTextColor(com.xuexiang.xui.R.color.xui_config_color_white)
        }
    }

    private fun initObserver() {

        val bgView: ImageView = binding.playerBg
        val playDisc: ImageView = binding.playDisc
        val writeImg: ImageView = binding.playWrite
        // 歌词发生变化
        viewModel.lyricLiveData.observe(this) {
            val item = it.getOrNull()
            if (item != null) {
                viewModel.setLyric(item)
                binding.lyricViewX.loadLyric(viewModel.lyric.value?.lrc?.lyric)
            }
        }
        //歌词控件中的歌词点击
        binding.lyricViewX.apply {
            setDraggable(true, object : OnPlayClickListener {
                override fun onPlayClick(time: Long): Boolean {
                    //刷新歌词位置
                    binding.lyricViewX.updateTime(time)
                    //移动到媒体流中的新位置,以毫秒为单位
                    StarrySky.with().seekTo(time)
                    return true
                }
            })
        }
        //监听音乐歌曲发生变化
        shareViewModel.nowPlayerSongLiveData.observe(this) {
            val item = it.value
            binding.apply {
                playerSongTitle.text = item?.name
                playerSongSubtitle.text = item?.ar?.get(0)?.name
            }
            //设置高斯模糊背景图\设置转盘图
            Glide.with(this).apply {
                load(item?.al?.picUrl).apply(
                    RequestOptions.bitmapTransform(
                        BlurTransformation(
                            60,
                            60
                        )
                    )
                ).into(bgView)
                load(R.drawable.play_disc).apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .into(playDisc)
                load(item?.al?.picUrl).apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .into(writeImg)
            }
            item?.id?.let { it1 -> viewModel.getLyric(it1) }
        }
        //监听播放状态
        shareViewModel.playStatusLiveData.observe(this) {
            when (it) {
                12 -> {
                    binding.playStart.setImageResource(R.drawable.icon_play_pause)
                }
                15 -> {
                    binding.playStart.setImageResource(R.drawable.icon_music_play)
                }
            }
        }
        //设置音乐播放进度
        StarrySky.with().setOnPlayProgressListener(object : OnPlayProgressListener {
            override fun onPlayProgress(currPos: Long, duration: Long) {
                if (shareViewModel.playerSongUrl.value?.data?.get(0)?.url==null) return
                val values = duration / 100
                if (currPos < values) return
                val progress = currPos.toInt() * 100 / duration.toInt()
                println(progress)
                binding.apply {
                    //刷新进度条
                    musicSeekBar.progress = progress
                    //设置当前播放时间
                    musicCur.text = currPos.toMinAndSeconds()
                    //设置歌曲时长
                    binding.musicLength.text =StarrySky.with().getDuration().toMinAndSeconds()
                    lyricViewX.setCurrentTextSize(60f)
                    //刷新歌词位置
                    lyricViewX.updateTime(currPos)
                }
            }
        })
        // 歌曲进度条变化的监听
        binding.musicSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                //如果是由用户触发
                if (fromUser) {
                    //进度条变化刷新歌词位置（进度*总长/100）得到当前播放时长
                    val location: Long = (StarrySky.with().getDuration() * progress) / 100
                    println(location)
                    //刷新歌词位置
                    binding.lyricViewX.updateTime(location)
                    //移动到媒体流中的新位置,以毫秒为单位
                    StarrySky.with().seekTo(location)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    /**
     * 歌词与转盘 事件
     */
    private fun initListener() {

        val intent = Intent(CTL_ACTION)
        binding.apply {
            // 转盘点击
            discContainer.setOnClickListener {
                if (it.isVisible) {
                    it.goneAlphaAnimation()
                    binding.lyricContainer.visibleAlphaAnimation()
                } else {
                    it.visibleAlphaAnimation()
                    binding.lyricContainer.goneAlphaAnimation()
                };
            }
            // 歌词点击
            lyricViewX.setOnSingerClickListener(object : OnSingleClickListener {
                override fun onClick() {
                    binding.lyricContainer.goneAlphaAnimation()
                    binding.discContainer.visibleAlphaAnimation()
                    binding.lyricContainer.clearAnimation()
                }
            })
            //评论
//            musicComment.setOnClickListener {
//                val intents =Intent(this@PlayerActivity, CommentActivity::class.java)
//                intents.putExtra("id",shareViewModel.getNowPlayInfo?.id?.toString())
//                startActivity(intents)
//            }
            //播放列表
            playerNowPlaylist.setOnClickListener {
                dialog.show();
                WidgetUtils.transparentBottomSheetDialogBackground(dialog);
            }
            // 返回按钮
            playSongIconDown.setOnClickListener {
                finish()
                this@PlayerActivity.overridePendingTransition(
                    0,
                    R.anim.activity_close
                )
            }
            //向service发送上一首指令
            playLast.setOnClickListener {
                intent.putExtra("control", 1)
                //发送广播，将被Service中的BroadcastReceiver接收到
                sendBroadcast(intent);
            }
            //向service发送播放、暂停指令
            playStart.setOnClickListener {
                createPlay(intent)
            }
            //向service发送下一首指令
            playNext.setOnClickListener {
                intent.putExtra("control", 4)
                sendBroadcast(intent);
            }
            //根据传递的参数判断是从music_tab 进来的还是，从播放歌单进来的
            if(params !="yes"){
                //进入立即播放
                Timer().schedule(200) {
                    createPlay(intent)
                }
            }else startRotateAlways()
        }
    }

    private fun createPlay(intent: Intent) {
        val status = shareViewModel.getPlayStatus
        if (status == 11 || status == 15) {
            //没有播放或暂停时去播放
            intent.putExtra("control", 2);
        } else if (status == 12) {
            //正在播放
            intent.putExtra("control", 3);
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
        // 注册service
        val intent = Intent(this, MusicService::class.java)
        startService(intent)
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
                startRotateAlways()
                shareViewModel.changeStatus(12)
            } else if (update == 15) {
                pauseRotateAlways()
                shareViewModel.changeStatus(15)
            }
        }
    }

    //关闭页面动画
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        this@PlayerActivity.overridePendingTransition(
            0, R.anim.activity_close
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(musicBroadcastReceiver)   // 必须要在onDestroy时反注册，否则会内存泄漏
    }

    //开启旋转动画
    private fun startRotateAlways() {
        objectAnimator.resume()
    }

    // 关闭旋转动画
    private fun pauseRotateAlways() {
        objectAnimator.pause()
    }

    companion object {
        const val CTL_ACTION = "com.jhp.cloudmusic.MUSIC_BROADCAST"
        const val UPDATE_ACTION = "com.jhp.cloudmusic.UPDATE_ACTION"

        // 动画循环时长
        private const val DURATION_CD = 32_000L
        private const val ANIMATION_REPEAT_COUNTS = -1
        private const val ANIMATION_PROPERTY_NAME = "rotation"
    }

    private val objectAnimator: ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(binding.discContainer, ANIMATION_PROPERTY_NAME, 0f, 360f).apply {
            interpolator = LinearInterpolator()
            duration = DURATION_CD
            repeatCount = ANIMATION_REPEAT_COUNTS
            start()
        }
    }
}