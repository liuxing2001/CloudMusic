package com.jhp.cloudmusic.ui.home

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.activityViewModels
import com.jhp.cloudmusic.databinding.FragmentMusicTabBarBinding
import com.jhp.cloudmusic.ui.player.PlayerActivity
import com.jhp.cloudmusic.viewmodel.SharedViewModel
import com.lzx.starrysky.SongInfo
import com.lzx.starrysky.StarrySky


class MusicTabFragment : Fragment() {
    companion object {
        // 动画循环时长
        private const val DURATION_CD = 32_000L
        private const val ANIMATION_REPEAT_COUNTS = -1
        private const val ANIMATION_PROPERTY_NAME = "rotation"
    }

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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicTabBarBinding.inflate(inflater, container, false)

        initListener()


        return _binding.root
    }



    private fun initListener() {
        binding.apply {
            musicTabHead.setOnClickListener {
                goPlayerPage()
            }
            musicTabPlay.setOnClickListener {
                objectAnimator.resume()

//            val info = SongInfo()
//            info.songId = "111"
//            info.songUrl = "http://music.163.com/song/media/outer/url?id=317151.mp3"
//            StarrySky.with().playMusicByInfo(info)
            }
        }

    }


    private fun goPlayerPage(){

            val intent = Intent(activity, PlayerActivity::class.java).apply {
                putExtra("MusicTab","yes")
            }
            startActivity(intent)

    }

}