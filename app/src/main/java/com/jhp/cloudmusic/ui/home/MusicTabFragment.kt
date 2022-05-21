package com.jhp.cloudmusic.ui.home

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jhp.cloudmusic.R
import com.jhp.cloudmusic.databinding.FragmentMusicTabBarBinding
import com.jhp.cloudmusic.ui.common.adapter.KotlinDataAdapter
import com.jhp.cloudmusic.ui.player.PlayerActivity
import com.jhp.cloudmusic.ui.SharedViewModel
import com.lzx.starrysky.SongInfo
import com.lzx.starrysky.StarrySky
import com.xuexiang.xui.utils.WidgetUtils


class MusicTabFragment : Fragment() {
    companion object {
        // 动画循环时长
        private const val DURATION_CD = 32_000L
        private const val ANIMATION_REPEAT_COUNTS = -1
        private const val ANIMATION_PROPERTY_NAME = "rotation"
    }
    private lateinit var dialog: BottomSheetDialog
    private lateinit var adapter: KotlinDataAdapter<SongInfo>
    private lateinit var bottomView: View
    private val playerController = StarrySky.with()
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private lateinit var _binding: FragmentMusicTabBarBinding
    val binding: FragmentMusicTabBarBinding
        get() = _binding

    //旋转动画
    private val objectAnimator: ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(
            binding.musicTabDisc,
            ANIMATION_PROPERTY_NAME, 0f, 360f
        ).apply {
            interpolator = LinearInterpolator()
            duration = DURATION_CD
            repeatCount = ANIMATION_REPEAT_COUNTS
            start()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicTabBarBinding.inflate(inflater, container, false)
        initView()
        initListener()
        initObserver()
        return _binding.root
    }

     @SuppressLint("SetTextI18n")
     fun initView() {

        bottomView = LayoutInflater.from(activity).inflate(R.layout.bottom_sheet_dialog_list, null)
        val recyclerView: RecyclerView = bottomView.findViewById(R.id.bottom_sheet_RecyclerView)
        val layoutManager = LinearLayoutManager(activity)
        adapter = KotlinDataAdapter.Builder<SongInfo>()
            .setLayoutId(R.layout.layout_song_item)
            .setData(sharedViewModel.mediaPlayerList as ArrayList)
            .addBindView { itemView, itemData, index ->
                val songTitle: TextView = itemView.findViewById(R.id.songTitle)
                val songSubTitle: TextView = itemView.findViewById(R.id.songTotalTime)
                val songImg: TextView = itemView.findViewById(R.id.songPic)
                songTitle.text = itemData.songName
                songSubTitle.text = itemData.artist
                songImg.text = "${index+1}"
                itemView.setOnClickListener {
                    //播放音乐
                    playerController.playMusicById(itemData.songId)
                    dialog.hide()
                }
            }
            .create()

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        bottomView.findViewById<TextView>(R.id.bottom_sheet_title).text ="播放列表"
        dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(bottomView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }

     private fun initObserver() {

        //监听当前播放状态
        playerController.playbackState().observe(viewLifecycleOwner) {
            //当前音频播放状态
            if (it.stage == "PLAYING") {
                //拿到前音频信息
                val item = playerController.getNowPlayingSongInfo()
                item?.let {
                    Glide.with(this).load(item.songCover).into(binding.musicTabDisc)
                    binding.apply {
                        musicTabTitle.text = item.songName
                        musicTabSubtitle.text = item.artist
                    }
                }
                binding.musicTabPlay.setImageResource(R.drawable.icon_play_pause)
                objectAnimator.resume()
                //根据当前播放音乐的index，拿到，播放器播放队列歌单(NowPlayInfo)
                val index = StarrySky.with().getNowPlayingIndex()
                val obj = sharedViewModel.nowPlayerList[index]
                //设置当前播放音乐，以便于跳转播放界面后监听
                sharedViewModel.setNowPlayerSong(obj)
            } else {
                binding.musicTabPlay.setImageResource(R.mipmap.music_tab_play)
                objectAnimator.pause()
            }
        }
     }

     private fun initListener() {

        binding.apply {
//            //上一首
//            musicTabBefore.setOnClickListener {
//                if (playerController.isPlaying()) playerController.stopMusic()
//                playerController.skipToPrevious()
//            }
            //播放、暂停
            musicTabPlay.setOnClickListener {
                if (playerController.isPlaying()) {
                    playerController.pauseMusic()
                } else {
                    playerController.restoreMusic()
                }
            }
//            //下一首
//            musicTabNext.setOnClickListener {
//                if (playerController.isPlaying()) playerController.stopMusic()
//                playerController.skipToNext()
//            }
            //播放列表
            musicTabList.setOnClickListener {
                dialog.show()
                WidgetUtils.transparentBottomSheetDialogBackground(dialog);
            }
            musicTabDisc.setOnClickListener{
                goPlayerPage()
            }
            musicTabHead.setOnClickListener {
                goPlayerPage()
            }
        }
    }
    //跳转到播放器界面
    private fun goPlayerPage(){
        if(sharedViewModel.getNowPlayInfo != null){
            val intent = Intent(activity,PlayerActivity::class.java).apply {
                putExtra("MusicTab","yes")
            }
            startActivity(intent)
        }
    }
}