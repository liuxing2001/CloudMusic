package com.jhp.cloudmusic.ui.home.mine

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.jhp.cloudmusic.R
import com.jhp.cloudmusic.dao.UserInfoDao
import com.jhp.cloudmusic.databinding.FragmentMineBinding
import com.jhp.cloudmusic.model.UserPlayList
import com.jhp.cloudmusic.ui.common.adapter.KotlinDataAdapter
import com.jhp.cloudmusic.ui.playlist.PlayListActivity
import com.jhp.cloudmusic.utils.XToastUtils
import com.xuexiang.xui.utils.WidgetUtils
import com.xuexiang.xui.widget.dialog.MiniLoadingDialog
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet
import com.xuexiang.xui.widget.imageview.IconImageView


class MineFragment : Fragment() {
    private var userInfo = UserInfoDao.getUserInfo()
    private lateinit var mMiniLoadingDialog: MiniLoadingDialog
    private lateinit var adapter: KotlinDataAdapter<UserPlayList.Playlist>

    private var _binding: FragmentMineBinding? = null

    private val binding get() = _binding!!
    private val viewModel by lazy {
        ViewModelProvider(this)[MineViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMineBinding.inflate(inflater, container, false)
        initData()
        initView()
        initObserver()
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {
        //获取歌单
        mMiniLoadingDialog.show()
        viewModel.getPlayList(userInfo.account.id.toString())
        viewModel.playListLiveData.observe(viewLifecycleOwner) {
            val playList = it.getOrNull()
            if (playList != null) {
                viewModel.playList.clear()
                viewModel.playList.addAll(playList)
                adapter.notifyDataSetChanged()
            }
            mMiniLoadingDialog.hide()
            binding.findFragmentRefresh.isRefreshing = false
        }
        //下拉刷新
        binding.findFragmentRefresh.apply {
            setColorSchemeResources(R.color.color_F71816)
            setOnRefreshListener {
                viewModel.getPlayList(userInfo.account.id.toString())
            }
        }
    }

    private fun initView() {
        mMiniLoadingDialog = WidgetUtils.getMiniLoadingDialog(requireContext(), "努力加载中")
        binding.findRecyclerView.layoutManager = GridLayoutManager(activity, 2)
    }

    @SuppressLint("SetTextI18n")
    private fun initData() {

        //  使用通用KTAdapter 构建 适配器
        adapter = KotlinDataAdapter.Builder<UserPlayList.Playlist>()
            .setData(viewModel.playList)
            .setLayoutId(R.layout.layout_item_song_card)
            .addBindView { itemView, itemData ,_->
                val image = itemView.findViewById(R.id.iv_song_image) as ImageView
                val userName = itemView.findViewById(R.id.tv_user_name) as TextView
                val read = itemView.findViewById(R.id.tv_read) as TextView
                val more = itemView.findViewById(R.id.songs_more) as IconImageView
                val itemSong = itemView.findViewById(R.id.item_song_card) as CardView
                userName.text = itemData.name
                read.text = "歌曲数:${itemData.trackCount}"
                Glide.with(this).load(itemData.coverImgUrl).apply(
                    RequestOptions.bitmapTransform(RoundedCorners(30))
                ).into(image)

                more.setOnClickListener {
                    showSimpleBottomSheetList(itemData)
                }
                itemSong.setOnClickListener {
                    val intent = Intent(context, PlayListActivity::class.java).apply {
                        putExtra("id", itemData.id.toString())
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
        binding.findRecyclerView.adapter = adapter
    }

    private fun showSimpleBottomSheetList(itemData: UserPlayList.Playlist) {
        BottomSheet.BottomListSheetBuilder(activity)
            .setTitle(itemData.name)
            .addItem("修改资料")
            .addItem("删除")
            .setIsCenter(false)
            .setOnSheetItemClickListener { dialog: BottomSheet, _: View?, position: Int, tag: String? ->
                dialog.dismiss()
                XToastUtils.toast("Item " + (position + 1))
            }
            .build()
            .show()
    }
}