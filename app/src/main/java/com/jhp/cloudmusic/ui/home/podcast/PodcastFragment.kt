package com.jhp.cloudmusic.ui.home.podcast

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.jhp.cloudmusic.R
import com.jhp.cloudmusic.data.model.DjBanner
import com.jhp.cloudmusic.data.model.RecommendDj
import com.jhp.cloudmusic.databinding.FragmentPodcastBinding
import com.jhp.cloudmusic.ui.common.adapter.KotlinDataAdapter
import com.jhp.cloudmusic.ui.common.view.LoadingObserver
import com.jhp.cloudmusic.ui.playlist.podcast.PodCastListActivity
import com.jhp.cloudmusic.utils.XToastUtils
import com.jhp.cloudmusic.utils.extension.load
import com.xuexiang.xui.widget.banner.widget.banner.BannerItem
import com.xuexiang.xui.widget.banner.widget.banner.base.BaseBanner

class PodcastFragment : Fragment(),BaseBanner.OnItemClickListener<BannerItem> {

    private lateinit var _binding: FragmentPodcastBinding
    private lateinit var cardAdapter: KotlinDataAdapter<RecommendDj.DjRadio>
    private lateinit var mData: List<BannerItem>

    private val binding: FragmentPodcastBinding
        get() = _binding

    private val viewModel by lazy {
        ViewModelProvider(this)[PodcastViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_podcast, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        _binding.vm = viewModel


        initData()
        initObserver()
        return _binding.root
    }


    @SuppressLint("SetTextI18n")
    fun initData() {


        cardAdapter = KotlinDataAdapter.Builder<RecommendDj.DjRadio>()
            .setLayoutId(R.layout.item_podcast_dj)
            .setData(viewModel.recommendDjList)
            .addBindView { itemView, itemData, index ->
                val name: TextView = itemView.findViewById(R.id.tv_item_album_song_name)
                val image: ImageView = itemView.findViewById(R.id.iv_item_album_song)
                val artist: TextView = itemView.findViewById(R.id.tv_item_album_song_artist)
                val playCount: TextView = itemView.findViewById(R.id.tv_item_album_song_playnum)
                name.text = itemData.name
                image.load(itemData.picUrl)
                artist.text = itemData.dj.nickname
                playCount.text = itemData.playCount.toString()
                itemView.setOnClickListener {
                    val intent = Intent(context, PodCastListActivity::class.java).apply {
                        putExtra("id", itemData.id)
                        putExtra("bgUrl", itemData.picUrl)
                        putExtra("title", itemData.name)
                        putExtra("avUrl", itemData.dj.avatarUrl)
                        putExtra("author", itemData.dj.nickname)
                        putExtra("music_count", itemData.programCount.toString())
//                        putExtra("description", itemData.)
                        putExtra(" play_count", itemData.playCount.toString())

                    }
                    startActivity(intent)
                }
            }
            .create()
        //绑定layoutManager、adapter
        binding.rvRadioLoading.apply {
            layoutManager = GridLayoutManager(activity, 3)
            adapter = cardAdapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initObserver() {


        viewModel.getRecommendDj()

        viewModel.djBannerLiveData.observe(viewLifecycleOwner) {
            val item = it.getOrNull()
            if (item != null) {
                viewModel.setDjBanner(item.data)
                mData = viewModel.djBannerList.map {
                    BannerItem().apply {
                        this.imgUrl = it.pic
                        this.title = "ok"
                    }
                }


                binding.sibCornerRectangle
                    .setSource(mData)
                    .setOnItemClickListener(this)
                    .startScroll()
            }
            // cardAdapter.notifyDataSetChanged()

        }
        viewModel.recommendDjLiveData.observe(viewLifecycleOwner) {
            val item = it.getOrNull()
            if (item != null) {
                viewModel.setRecommendDj(item.djRadios)

                cardAdapter.notifyDataSetChanged()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner, LoadingObserver(requireContext()))

    }
    override fun onItemClick(view: View?, item: BannerItem?, position: Int) {
        XToastUtils.toast("position--->" + position + ", item:" + item!!.title)
    }
}