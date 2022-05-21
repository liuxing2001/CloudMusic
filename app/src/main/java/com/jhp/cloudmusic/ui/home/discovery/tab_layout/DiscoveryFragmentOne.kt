package com.jhp.cloudmusic.ui.home.discovery.tab_layout

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson

import com.jhp.cloudmusic.R
import com.jhp.cloudmusic.databinding.FragmentDiscoveryOneBinding
import com.jhp.cloudmusic.data.model.NowPlayInfo
import com.jhp.cloudmusic.data.model.RecommendSongs
import com.jhp.cloudmusic.ui.common.adapter.KotlinDataAdapter
import com.jhp.cloudmusic.ui.player.PlayerActivity
import com.jhp.cloudmusic.ui.SharedViewModel

class DiscoveryFragmentOne : Fragment() {
    private lateinit var adapter: KotlinDataAdapter<RecommendSongs.Data.DailySong>
    private lateinit var _binding: FragmentDiscoveryOneBinding
    private val viewModel by lazy {
        ViewModelProvider(this).get(DiscoveryFragmentOneViewModel::class.java)
    }
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    val binding: FragmentDiscoveryOneBinding
        get() = _binding

    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoveryOneBinding.inflate(inflater, container, false)
        initData()
        initObserver()
        return _binding.root
    }

    @SuppressLint("SetTextI18n")
     fun initData() {

        val layoutManager = LinearLayoutManager(activity)
        _binding.topRecyclerView.layoutManager = layoutManager
        adapter = KotlinDataAdapter.Builder<RecommendSongs.Data.DailySong>()
            .setLayoutId(R.layout.layout_song_item)
            .setData(viewModel.recommendSongsList)
            .addBindView { itemView, itemData, index ->
                val songIndex = itemView.findViewById<TextView>(R.id.songPic)
                val title = itemView.findViewById<TextView>(R.id.songTitle)
                val subTitle = itemView.findViewById<TextView>(R.id.songTotalTime)
                title.text = itemData.name
                subTitle.text = "${itemData.ar[0].name} - ${itemData.al.name}"
                songIndex.text = "${index + 1}"
                itemView.setOnClickListener {
                    val gson = Gson()
                    val obj = gson.fromJson(gson.toJson(itemData), NowPlayInfo::class.java)
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
        _binding.topRecyclerView.adapter = adapter
    }

     private fun initObserver() {

        viewModel.getRecommendSongs()
        viewModel.recommendListLiveData.observe(viewLifecycleOwner, Observer {
            val list = it.getOrNull()
            if (list != null) {
                viewModel.recommendSongsList.addAll(list)
                adapter.notifyDataSetChanged()
            }
            binding.moreFragmentRefresh.isRefreshing = false
        })
        binding.moreFragmentRefresh.apply {
            setColorSchemeResources(R.color.color_F71816)
            setOnRefreshListener {
                viewModel.getRecommendSongs()
            }
        }
    }
}