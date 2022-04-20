package com.jhp.cloudmusic.ui.player

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.jhp.cloudmusic.databinding.ActivityPlayerBinding
import com.jhp.cloudmusic.utils.StatusBarUtils


class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initBar()
    }

    private fun initBar() {
        StatusBarUtils.setStatusBarDarkMode(this)
    }

    private fun initBinding() {
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        StatusBarUtils.translucent(this, Color.TRANSPARENT)
    }
}