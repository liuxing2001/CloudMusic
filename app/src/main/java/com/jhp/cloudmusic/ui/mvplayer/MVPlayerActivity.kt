package com.jhp.cloudmusic.ui.mvplayer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jzvd.Jzvd
import com.bumptech.glide.Glide
import com.jhp.cloudmusic.R
import com.jhp.cloudmusic.databinding.ActivityMvPlayerBinding
import com.jhp.cloudmusic.extension.visible
import com.jhp.cloudmusic.model.MusicComment
import com.jhp.cloudmusic.model.RecommendSongs
import com.jhp.cloudmusic.ui.common.adapter.KotlinDataAdapter
import com.jhp.cloudmusic.ui.home.mine.MineViewModel
import com.lzx.starrysky.utils.contextReflex
import com.xuexiang.xui.utils.WidgetUtils
import com.xuexiang.xui.widget.button.shinebutton.ShineButton
import com.xuexiang.xui.widget.dialog.MiniLoadingDialog
import com.xuexiang.xui.widget.imageview.RadiusImageView


class MVPlayerActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMvPlayerBinding
    private lateinit var mMiniLoadingDialog: MiniLoadingDialog
    private lateinit var adapter: KotlinDataAdapter<MusicComment.Comment>


    private val binding:  ActivityMvPlayerBinding
        get() = _binding

    private lateinit var mvId: String
    private lateinit var  time: String
    private lateinit var title: String
    private lateinit var cover: String

    private val viewModel by lazy {
        ViewModelProvider(this)[MVPlayerViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initData()
        initView()
        initObserver()
    }


    private fun initBinding() {
        _binding = ActivityMvPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    private fun initData() {
        mvId= intent.getStringExtra("id").toString()
        time= intent.getStringExtra("time").toString()
        title = intent.getStringExtra("title").toString()
        cover = intent.getStringExtra("cover").toString()
        binding.tvVideoDetailTitle.text = title
        binding.tvVideoDetailPlaytime.text = time

        //一定不要忘记设置layoutManager！！！！！！！！
        val layoutManager = LinearLayoutManager(this)
        _binding.rvVideoComment.layoutManager = layoutManager

    }

    private fun initView() {
        mMiniLoadingDialog = WidgetUtils.getMiniLoadingDialog(this, "努力加载中")

        //  使用通用KTAdapter 构建 适配器
        adapter = KotlinDataAdapter.Builder<MusicComment.Comment>()
            .setData(viewModel.commentList)
            .setLayoutId(R.layout.item_gedan_detail_comment)
            .addBindView { itemView, itemData, _ ->
                val img = itemView.findViewById<ImageView>(R.id.iv_item_gedan_comment_avatar_img)
                val name = itemView.findViewById<TextView>(R.id.tv_item_gedan_comment_avatar_name)
                val time = itemView.findViewById<TextView>(R.id.tv_item_gedan_comment_time)
                val likedCount = itemView.findViewById<TextView>(R.id.tv_item_gedan_comment_zan_count)
                val comment = itemView.findViewById<TextView>(R.id.tv_item_gedan_comment_content)
                val vipRights = itemView.findViewById<ImageView>(R.id.iv_item_gedan_comment_avatar_vip)
                Glide.with(this).load(itemData.user.avatarUrl).into(img)
                name.text = itemData.user.nickname
                time.text = itemData.timeStr
                comment.text = itemData.content
                likedCount.text = itemData.likedCount.toString()
                @Suppress("SENSELESS_COMPARISON")
                if (itemData.user.vipRights!=null){
                    vipRights.visible()
                }
            }
            .create()
        binding.rvVideoComment.adapter = adapter


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {
        mMiniLoadingDialog.show()
        viewModel.setMvId(mvId)

        viewModel.mvUrlLiveData.observe(this){
            val mvUrl = it.getOrNull()
            if (mvUrl!=null){
                viewModel.mvUrl.value = mvUrl.data
            }
            binding.jzVideo.setUp(
                viewModel.mvUrl.value?.url,
                title
            )
            Glide.with(this).load(cover).into(binding.jzVideo.posterImageView)

            mMiniLoadingDialog.hide()
        }
        viewModel.mvCountDetailLiveData.observe(this){
            val item = it.getOrNull()

            if(item!=null){
                binding.tvVideoDetailPraiseCount.text = item.likedCount.toString()
                binding.tvVideoDetailShareCount.text = item.shareCount.toString()
                binding.tvVideoDetailCommentCount.text = item.commentCount.toString()
            }
        }

        viewModel.mvCommentLiveData.observe(this) {
            val item = it.getOrNull()
            if (item != null) {
                viewModel.commentList.addAll(item.comments)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onBackPressed() {
        if (Jzvd.backPress()) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        Jzvd.releaseAllVideos();
    }
}