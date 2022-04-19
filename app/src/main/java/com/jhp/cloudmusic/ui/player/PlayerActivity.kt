package com.jhp.cloudmusic.ui.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.jhp.cloudmusic.databinding.ActivityPlayerBinding


class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
    }

    private fun initBinding() {
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}