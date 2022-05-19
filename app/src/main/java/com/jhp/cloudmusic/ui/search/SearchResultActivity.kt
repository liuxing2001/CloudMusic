package com.jhp.cloudmusic.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jhp.cloudmusic.databinding.ActivitySearchBinding
import com.jhp.cloudmusic.databinding.ActivitySearchResultBinding

class SearchResultActivity : AppCompatActivity() {
    private lateinit var _binding: ActivitySearchResultBinding
    private val binding: ActivitySearchResultBinding
        get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
    }

    private fun initBinding() {
        _binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}