package com.jhp.cloudmusic.ui.playlist.podcast



import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.jhp.cloudmusic.R
import com.jhp.cloudmusic.data.model.NewMusic

import com.jhp.cloudmusic.databinding.ActivityPlayListBinding
import com.jhp.cloudmusic.data.model.NowPlayInfo
import com.jhp.cloudmusic.data.model.SongList
import com.jhp.cloudmusic.databinding.ActivityPodcastListBinding
import com.jhp.cloudmusic.ui.MainActivity
import com.jhp.cloudmusic.ui.common.adapter.KotlinDataAdapter
import com.jhp.cloudmusic.ui.player.PlayerActivity
import com.jhp.cloudmusic.utils.StatusBarUtils
import com.jhp.cloudmusic.ui.SharedViewModel
import com.jhp.cloudmusic.utils.extension.load
import com.lzx.starrysky.utils.formatTime

import com.xuexiang.xui.utils.WidgetUtils
import com.xuexiang.xui.widget.dialog.MiniLoadingDialog

class PodCastListActivity : AppCompatActivity() {
    private var mMiniLoadingDialog: MiniLoadingDialog? = null
    private lateinit var avUrl:String
    private val viewModel by lazy {
        ViewModelProvider(this)[PodCastListViewModel::class.java]
    }
    private lateinit var adapter: KotlinDataAdapter<DjProgram.Program>
    private lateinit var _binding: ActivityPodcastListBinding
    private val binding: ActivityPodcastListBinding
        get() = _binding

    private val sharedViewModel by lazy {
        MainActivity.mainActivity?.let { ViewModelProvider(it)[SharedViewModel::class.java] }!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initData()
        initView()
        initObserver()
        initBar()
    }

    @SuppressLint("SetTextI18n")
    private fun initData() {
        val songsId: String? = intent.getStringExtra("id")
        val bgUrl: String? = intent.getStringExtra("bgUrl")
        avUrl = intent.getStringExtra("avUrl").toString()
        val title: String? = intent.getStringExtra("title")
        val author: String? = intent.getStringExtra("author")
        val musicCount: String? = intent.getStringExtra("music_count")
        var playCount: String? = intent.getStringExtra("play_count")

        //数字过大去掉后四位 + 万 大于
        if (playCount != null) {
            if (playCount.length >= 9) {
                playCount = playCount.substring(0, playCount.length - 8) + "亿"
            } else if (playCount.length >= 5) {
                playCount = playCount.substring(0, playCount.length - 4) + "万"
            }
        }

        if (songsId != null) {
            mMiniLoadingDialog?.show()
            viewModel.getPlayListTrack(songsId)
            Glide.with(this).load(bgUrl).into(binding.ivGedanDetailImg)
            Glide.with(this).load(avUrl).into(binding.ivGedanDetailAvatarImg)
            binding.tvGedanDetailTitle.text = title
            binding.tvGedanDetailAvatarName.text = author
            binding.tvGedanDetailSongNum.text = " ($musicCount)"
            binding.tvGedanDetailPlaynum.text = playCount
         //   binding.tvGedanDetailDesc.text = description?:""
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {


        adapter = KotlinDataAdapter.Builder<DjProgram.Program>()
            .setLayoutId(R.layout.item_podcast_song)
            .setData(viewModel.djProgramList)
            .addBindView { itemView, itemData, index ->
                //绑定数据
                val songTitle: TextView = itemView.findViewById(R.id.pod_cast_song_title)
                val songTotalTime: TextView = itemView.findViewById(R.id.pod_cast_song_total_time)
                val songPic: ImageView = itemView.findViewById(R.id.pod_cast_song_pic)
                val songPlayTime:TextView = itemView.findViewById(R.id.pod_cast_song_play_Num)
                songTitle.text = itemData.mainSong.name
                songTotalTime.text = itemData.mainSong.duration.formatTime()
                songPlayTime.text = "▷" + itemData.mainSong.bMusic.playTime
                songPic.load(avUrl)
                //请求音乐源，将当前音乐信息保存到shareViewModel中，便于下个界面请求歌词等等
                itemView.setOnClickListener {
                    val gson = Gson()
                    val obj = gson.fromJson(gson.toJson(itemData),NowPlayInfo::class.java)
                    nowPlayInfoFormat(obj,itemData)
                    sharedViewModel.setNowPlayerSong(obj)
                    sharedViewModel.setPlayerSongId(obj.id.toString())
                    sharedViewModel.playerSongUrlLiveData.observe(this@PodCastListActivity) {
                        val item = it.getOrNull()
                        if (item != null) {
                            sharedViewModel.playerSongUrl.value = item
                        }
                    }
                    val intent = Intent(this@PodCastListActivity, PlayerActivity::class.java)
                    startActivity(intent)
                }
            }
            .create()
        binding.programDetailListRecyclerView.adapter = adapter
        binding.programDetailListRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {

        viewModel.djProgramListLiveData.observe(this) {
            val list = it.getOrNull()
            if (list != null) {
                viewModel.djProgramList.addAll(list.programs)
                adapter.notifyDataSetChanged()
            }
            mMiniLoadingDialog?.hide()
        }
    }

    private fun initBinding() {
        _binding = ActivityPodcastListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        StatusBarUtils.translucent(this, Color.TRANSPARENT)
        mMiniLoadingDialog = WidgetUtils.getMiniLoadingDialog(this, "努力加载中");

    }

    private fun initBar() {

        StatusBarUtils.setStatusBarDarkMode(this)
    }


    override fun onDestroy() {
        mMiniLoadingDialog?.dismiss()
        super.onDestroy()
    }
    private fun nowPlayInfoFormat(obj: NowPlayInfo, itemData: DjProgram.Program) {

        val ar: NowPlayInfo.Ar = NowPlayInfo.Ar(1, "")

        val al: NowPlayInfo.Al = NowPlayInfo.Al("", "", 1, "", "", listOf())
        obj.name = itemData.mainSong.name
        obj.id = itemData.mainSong.id.toString()
        obj.ar = listOf(ar)
        obj.ar[0].id = itemData.mainSong.artists[0].id
        obj.ar[0].name = itemData.mainSong.artists[0].name
        obj.al = al
        obj.al.name = itemData.mainSong.album.name
        obj.al.id = itemData.mainSong.album.id.toString()
        obj.al.picUrl = itemData.mainSong.album.picUrl
    }
}