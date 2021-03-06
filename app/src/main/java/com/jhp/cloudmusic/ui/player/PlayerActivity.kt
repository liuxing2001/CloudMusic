package com.jhp.cloudmusic.ui.player

import android.animation.ObjectAnimator
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.jhp.cloudmusic.utils.extension.goneAlphaAnimation
import com.jhp.cloudmusic.utils.extension.toMinAndSeconds
import com.jhp.cloudmusic.utils.extension.visibleAlphaAnimation
import com.jhp.cloudmusic.service.MusicService
import com.jhp.cloudmusic.ui.MainActivity
import com.jhp.cloudmusic.ui.common.adapter.KotlinDataAdapter
import com.jhp.cloudmusic.utils.StatusBarUtils
import com.jhp.cloudmusic.ui.SharedViewModel
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

    //    ?????????????????????
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
        StatusBarUtils.translucent(this)
    }

    private fun initBinding() {

        _binding = ActivityPlayerBinding.inflate(layoutInflater)
        params = intent.getStringExtra("MusicTab")
        setContentView(binding.root)
    }

     private fun initData() {

        //  ?????????????????????
        bottomView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog_list, null)
        // ??????????????? ?????????adapter???layout manager???
        val recyclerView: RecyclerView = bottomView.findViewById(R.id.bottom_sheet_RecyclerView)
        val layoutManager = LinearLayoutManager(this)
        adapter = KotlinDataAdapter.Builder<SongInfo>()
            .setLayoutId(R.layout.layout_song_item)
            .setData(shareViewModel.mediaPlayerList as ArrayList<SongInfo>)
            .addBindView { itemView, itemData, index ->
                val songIndex : TextView = itemView.findViewById(R.id.songPic)
                val songTitle: TextView = itemView.findViewById(R.id.songTitle)
                val songSubTitle: TextView = itemView.findViewById(R.id.songTotalTime)
                songTitle.text = itemData.songName
                songSubTitle.text = itemData.artist
                songIndex.text = "${index+1}"
                itemView.setOnClickListener {
                    //????????????
                    val intent: Intent = Intent(CTL_ACTION)
                    intent.putExtra("control", 2);
                    sendBroadcast(intent);
                    dialog.hide()
                }
            }
            .create()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        bottomView.findViewById<TextView>(R.id.bottom_sheet_title).text = "????????????"
        dialog = BottomSheetDialog(this@PlayerActivity)
        dialog.setContentView(bottomView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }

     private fun initView() {


        // ???????????????????????????
        binding.lyricViewX.apply {
            setLabel(shareViewModel.getNowPlayInfo!!.name)
            //???????????????????????????????????? [normalColor]
            setNormalColor(com.xuexiang.xui.R.color.xui_btn_green_normal_color)
            //???????????????????????????????????????
            setTimelineColor(com.xuexiang.xui.R.color.xui_config_color_white)
            //?????????????????????????????????????????????
            setTimeTextColor(com.xuexiang.xui.R.color.xui_config_color_white)
            //????????????????????????????????????????????????
            setTimelineTextColor(com.xuexiang.xui.R.color.xui_config_color_white)
        }
    }

    private fun initObserver() {

        val bgView: ImageView = binding.playerBg
        val playDisc: ImageView = binding.playDisc

        // ??????????????????
        viewModel.lyricLiveData.observe(this) {
            val item = it.getOrNull()
            if (item != null) {
                viewModel.setLyric(item)
                binding.lyricViewX.loadLyric(viewModel.lyric.value?.lrc?.lyric)
            }
        }
        //??????????????????????????????
        binding.lyricViewX.apply {
            setDraggable(true, object : OnPlayClickListener {
                override fun onPlayClick(time: Long): Boolean {
                    //??????????????????
                    binding.lyricViewX.updateTime(time)
                    //?????????????????????????????????,??????????????????
                    StarrySky.with().seekTo(time)
                    return true
                }
            })
        }
        //??????????????????????????????
        shareViewModel.nowPlayerSongLiveData.observe(this) {
            val item = it.value
            binding.apply {
                playerSongTitle.text = item?.name
                playerSongSubtitle.text = item?.ar?.get(0)?.name
            }
            //???????????????????????????\???????????????
            Glide.with(this).apply {
                load(item?.al?.picUrl).apply(
                    RequestOptions.bitmapTransform(
                        BlurTransformation(
                            60,
                            60
                        )
                    )
                ).into(bgView)
                load(item?.al?.picUrl).apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .into(playDisc)

            }
            item?.id?.let { it1 -> viewModel.getLyric(it1.toInt()) }
        }
        //??????????????????
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
        //????????????????????????
        StarrySky.with().setOnPlayProgressListener(object : OnPlayProgressListener {
            override fun onPlayProgress(currPos: Long, duration: Long) {
                if (shareViewModel.playerSongUrl.value?.data?.get(0)?.url==null) return
                val values = duration / 100
                if (currPos < values) return
                val progress = currPos.toInt() * 100 / duration.toInt()
                println(progress)
                binding.apply {
                    //???????????????
                    musicSeekBar.progress = progress
                    //????????????????????????
                    musicCur.text = currPos.toMinAndSeconds()
                    //??????????????????
                    binding.musicLength.text =StarrySky.with().getDuration().toMinAndSeconds()
                    lyricViewX.setCurrentTextSize(60f)
                    //??????????????????
                    lyricViewX.updateTime(currPos)
                }
            }
        })
        // ??????????????????????????????
        binding.musicSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                //????????????????????????
                if (fromUser) {
                    //??????????????????????????????????????????*??????/100???????????????????????????
                    val location: Long = (StarrySky.with().getDuration() * progress) / 100
                    println(location)
                    //??????????????????
                    binding.lyricViewX.updateTime(location)
                    //?????????????????????????????????,??????????????????
                    StarrySky.with().seekTo(location)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    /**
     * ??????????????? ??????
     */
    private fun initListener() {

        val intent = Intent(CTL_ACTION)
        binding.apply {
            // ????????????
            discContainer.setOnClickListener {
                if (it.isVisible) {
                    it.goneAlphaAnimation()
                    binding.lyricContainer.visibleAlphaAnimation()
                } else {
                    it.visibleAlphaAnimation()
                    binding.lyricContainer.goneAlphaAnimation()
                };
            }
            // ????????????
            lyricViewX.setOnSingerClickListener(object : OnSingleClickListener {
                override fun onClick() {
                    binding.lyricContainer.goneAlphaAnimation()
                    binding.discContainer.visibleAlphaAnimation()
                    binding.lyricContainer.clearAnimation()
                }
            })
            //??????
//            musicComment.setOnClickListener {
//                val intents =Intent(this@PlayerActivity, CommentActivity::class.java)
//                intents.putExtra("id",shareViewModel.getNowPlayInfo?.id?.toString())
//                startActivity(intents)
//            }
            //????????????
            playerNowPlaylist.setOnClickListener {
                dialog.show();
                WidgetUtils.transparentBottomSheetDialogBackground(dialog);
            }
            // ????????????
            playSongIconDown.setOnClickListener {
                finish()
                this@PlayerActivity.overridePendingTransition(
                    0,
                    R.anim.activity_close
                )
            }
            //???service?????????????????????
            playLast.setOnClickListener {
                intent.putExtra("control", 1)
                //?????????????????????Service??????BroadcastReceiver?????????
                sendBroadcast(intent);
            }
            //???service???????????????????????????
            playStart.setOnClickListener {
                createPlay(intent)
            }
            //???service?????????????????????
            playNext.setOnClickListener {
                intent.putExtra("control", 4)
                sendBroadcast(intent)
            }
            //?????????????????????????????????music_tab ??????????????????????????????????????????
            if(params !="yes"){
                //??????????????????
                Timer().schedule(200) {
                    createPlay(intent)
                }
            }else startRotateAlways()
        }
    }

    private fun createPlay(intent: Intent) {
        val status = shareViewModel.getPlayStatus
        if (status == 11 || status == 15) {
            //?????????????????????????????????
            intent.putExtra("control", 2);
        } else if (status == 12) {
            //????????????
            intent.putExtra("control", 3);
        }
        sendBroadcast(intent)
    }

   private fun initBroadcastReceiver() {

        // ???????????????????????????????????????android.net.conn.CONNECTIVITY_CHANGE???????????????
        val intentFilter = IntentFilter()
        //??????BroadcastReceiver?????????Action
        intentFilter.addAction(UPDATE_ACTION)
        musicBroadcastReceiver = MusicBroadcastReceiver()
        //??????BroadcastReceiver
        registerReceiver(musicBroadcastReceiver, intentFilter)
        // ??????service
        val intent = Intent(this, MusicService::class.java)
        startService(intent)
    }

    //????????????BroadcastReceiver????????????????????????Service???????????????
    inner class MusicBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            //??????Intent??????update?????????update??????????????????
            val update = intent.getIntExtra("update", -1)
            binding.musicLength.text = StarrySky.with().getDuration().toMinAndSeconds()
            //????????????????????????
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

    //??????????????????
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        this@PlayerActivity.overridePendingTransition(
            0, R.anim.activity_close
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(musicBroadcastReceiver)   // ????????????onDestroy????????????????????????????????????
    }

    //??????????????????
    private fun startRotateAlways() {
        objectAnimator.resume()
    }

    // ??????????????????
    private fun pauseRotateAlways() {
        objectAnimator.pause()
    }

    companion object {
        const val CTL_ACTION = "com.jhp.cloudmusic.MUSIC_BROADCAST"
        const val UPDATE_ACTION = "com.jhp.cloudmusic.UPDATE_ACTION"

        // ??????????????????
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