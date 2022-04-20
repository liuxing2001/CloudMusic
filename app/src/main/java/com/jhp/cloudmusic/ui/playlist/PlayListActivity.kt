package com.jhp.cloudmusic.ui.playlist

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.jhp.cloudmusic.R

import com.jhp.cloudmusic.databinding.ActivityPlayListBinding
import com.jhp.cloudmusic.model.NowPlayInfo
import com.jhp.cloudmusic.model.SongList
import com.jhp.cloudmusic.ui.MainActivity
import com.jhp.cloudmusic.ui.common.adapter.KotlinDataAdapter
import com.jhp.cloudmusic.ui.player.PlayerActivity
import com.jhp.cloudmusic.utils.StatusBarUtils
import com.jhp.cloudmusic.viewmodel.SharedViewModel

import com.xuexiang.xui.utils.WidgetUtils
import com.xuexiang.xui.widget.dialog.MiniLoadingDialog

class PlayListActivity : AppCompatActivity() {
    private var mMiniLoadingDialog: MiniLoadingDialog? = null

    private val viewModel by lazy {
        ViewModelProvider(this).get(PlayListViewModel::class.java)
    }
    private lateinit var adapter: KotlinDataAdapter<SongList.Song>
    private lateinit var _binding: ActivityPlayListBinding
    private val binding: ActivityPlayListBinding
        get() = _binding

    private val sharedViewModel by lazy {
        MainActivity.mainActivity?.let { ViewModelProvider(it)[SharedViewModel::class.java] }!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initData()
        initObserver()
        initBar()
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initData() {
        val songsId: String? = intent.getStringExtra("id")
        val bgUrl: String? = intent.getStringExtra("bgUrl")
        val avUrl: String? = intent.getStringExtra("avUrl")
        val title: String? = intent.getStringExtra("title")
        val author: String? = intent.getStringExtra("author")
        val musicCount: String? = intent.getStringExtra("music_count")
        val description: String? = intent.getStringExtra("description")
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
            binding.tvGedanDetailDesc.text = description?:""
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {

        val layoutManager = LinearLayoutManager(this)
        binding.songDetailListRecyclerView.layoutManager = layoutManager
        adapter = KotlinDataAdapter.Builder<SongList.Song>()
            .setLayoutId(R.layout.layout_song_item)
            .setData(viewModel.playSongList)
            .addBindView { itemView, itemData, index ->
                //绑定数据
                val songTitle: TextView = itemView.findViewById(R.id.songTitle)
                val songSubTitle: TextView = itemView.findViewById(R.id.songSubTitle)
                val songIndex: TextView = itemView.findViewById(R.id.songIndex)
                songTitle.text = itemData.name
                songSubTitle.text = "${itemData.ar[0].name} - ${itemData.al.name}"
                songIndex.text = "${index + 1}"
                //请求音乐源，将当前音乐信息保存到shareViewModel中，便于下个界面请求歌词等等
                itemView.setOnClickListener {
                    val gson = Gson()
                    val obj = gson.fromJson(gson.toJson(itemData), NowPlayInfo::class.java)
                    sharedViewModel.setNowPlayerSong(obj)
                    sharedViewModel.setPlayerSongId(obj.id.toString())
                    sharedViewModel.playerSongUrlLiveData.observe(this@PlayListActivity) {
                        val item = it.getOrNull()
                        if (item != null) {
                            sharedViewModel.playerSongUrl.value = item
                        }
                    }
                    val intent = Intent(this@PlayListActivity, PlayerActivity::class.java)
                    startActivity(intent)
                }
            }
            .create()
        binding.songDetailListRecyclerView.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {

        viewModel.playSongListLiveData.observe(this) {
            val list = it.getOrNull()
            if (list != null) {
                viewModel.playSongList.addAll(list)
                adapter.notifyDataSetChanged()
            }
            mMiniLoadingDialog?.hide()
        }
    }

    private fun initBinding() {
        _binding = ActivityPlayListBinding.inflate(layoutInflater)
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
}