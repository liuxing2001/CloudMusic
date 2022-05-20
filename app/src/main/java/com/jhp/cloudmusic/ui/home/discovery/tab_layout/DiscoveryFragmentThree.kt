package com.jhp.cloudmusic.ui.home.discovery.tab_layout

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.jhp.cloudmusic.R
import com.jhp.cloudmusic.databinding.FragmentDiscoveryThreeBinding
import com.jhp.cloudmusic.utils.extension.load
import com.jhp.cloudmusic.data.model.HotPlayList
import com.jhp.cloudmusic.data.model.NewMusic
import com.jhp.cloudmusic.data.model.NowPlayInfo
import com.jhp.cloudmusic.ui.SharedViewModel
import com.jhp.cloudmusic.ui.common.adapter.KotlinDataAdapter
import com.jhp.cloudmusic.ui.common.view.LoadingObserver
import com.jhp.cloudmusic.ui.player.PlayerActivity
import com.jhp.cloudmusic.ui.playlist.normal.PlayListActivity


class DiscoveryFragmentThree : Fragment() {
    private lateinit var _binding: FragmentDiscoveryThreeBinding
    private lateinit var hotListAdapter: KotlinDataAdapter<HotPlayList.PlayList>
    private lateinit var newMusicAdapter: KotlinDataAdapter<NewMusic.Data>
    val binding: FragmentDiscoveryThreeBinding
        get() = _binding
    private val viewModel by lazy {
        ViewModelProvider(this)[DiscoveryFragmentThreeViewModel::class.java]
    }
    private val sharedViewModel by activityViewModels<SharedViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoveryThreeBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        _binding.vm = viewModel
        initData()
        initObserver()
        return binding.root
    }

    private fun initData() {
        hotListAdapter = KotlinDataAdapter.Builder<HotPlayList.PlayList>()
            .setLayoutId(R.layout.item_discover_playlist)
            .setData(viewModel.hotPlayList)
            .addBindView { itemView, itemData, index ->
                val name: TextView = itemView.findViewById(R.id.tv_item_discover_des)
                val image: ImageView = itemView.findViewById(R.id.iv_item_discover)
                val playNum: TextView = itemView.findViewById(R.id.tv_item_discover_playnum)
                name.text = itemData.name
                image.load(itemData.coverImgUrl)
                playNum.text = itemData.playCount.toString()

                itemView.setOnClickListener {
                    val intent = Intent(context, PlayListActivity::class.java).apply {
                        putExtra("id", itemData.id)
                        putExtra("bgUrl", itemData.coverImgUrl)
                        putExtra("avUrl", itemData.creator.avatarUrl)
                        putExtra("author", itemData.creator.nickname)
                        putExtra("title", itemData.name)
                        putExtra("music_count", itemData.trackCount.toString())
                        putExtra("play_count", itemData.playCount)
                        putExtra("description", itemData.description)
                    }
                    startActivity(intent)
                }
            }
            .create()
        //绑定layoutManager、adapter
        binding.rvDiscoverPlaylist.apply {
            layoutManager = GridLayoutManager(activity, 3)
            adapter = hotListAdapter
        }

        newMusicAdapter = KotlinDataAdapter.Builder<NewMusic.Data>()
            .setLayoutId(R.layout.layout_song_item)
            .setData(viewModel.newMusic)
            .addBindView { itemView, itemData, index ->
                val songIndex = itemView.findViewById<TextView>(R.id.songIndex)
                val title = itemView.findViewById<TextView>(R.id.songTitle)
                val subTitle = itemView.findViewById<TextView>(R.id.songSubTitle)
                title.text = itemData.name
                subTitle.text = "${itemData.artists[0].name} - ${itemData.album.name}"
                songIndex.text = "${index + 1}"

                itemView.setOnClickListener {
                    val gson = Gson()
                    val obj: NowPlayInfo = gson.fromJson(gson.toJson(itemData), NowPlayInfo::class.java)
                    nowPlayInfoFormat(obj,itemData)
                    sharedViewModel.setNowPlayerSong(obj)
                    sharedViewModel.setPlayerSongId(obj.id.toString())
                    sharedViewModel.playerSongUrlLiveData.observe(viewLifecycleOwner) {
                        val item = it.getOrNull()
                        if (item != null) {
                            sharedViewModel.playerSongUrl.value = item
                        }
                    }
                    val intent = Intent(context, PlayerActivity::class.java)
                    startActivity(intent)
                }
            }
            .create()
        //绑定layoutManager、adapter
        binding.rvDiscoverAlbumSong.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = newMusicAdapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {
        viewModel.getHotList()

        viewModel.hotPlayListLiveData.observe(viewLifecycleOwner) {
            val item = it.getOrNull()
            if (item != null) {
                viewModel.setHotPlayList(item.playlists)
                hotListAdapter.notifyDataSetChanged()
            }
        }
        viewModel.newMusicLiveData.observe(viewLifecycleOwner) {
            val item = it.getOrNull()
            if (item != null) {
                viewModel.setNewMusic(item.data)
                newMusicAdapter.notifyDataSetChanged()
            }
        }


        viewModel.loading.observe(viewLifecycleOwner, LoadingObserver(requireContext()))
    }
//格式化为NowPlayInfo
    private fun nowPlayInfoFormat(obj: NowPlayInfo, itemData: NewMusic.Data) {
        val ar: NowPlayInfo.Ar = NowPlayInfo.Ar(1, "")

        val al: NowPlayInfo.Al = NowPlayInfo.Al("", "", 1, "", "", listOf())
        obj.ar = listOf(ar)
        obj.ar[0].id = itemData.artists[0].id
        obj.ar[0].name = itemData.artists[0].name
        obj.al = al
        obj.al.name = itemData.album.name
        obj.al.id = itemData.album.id.toString()
        obj.al.picUrl = itemData.album.picUrl
    }
}