package com.jhp.cloudmusic.ui.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

import com.jhp.cloudmusic.databinding.ActivitySearchBinding
import com.jhp.cloudmusic.ui.MainActivity
import com.jhp.cloudmusic.ui.SharedViewModel
import com.xiaoyouProject.searchbox.SearchFragment
import com.xiaoyouProject.searchbox.entity.CustomLink
import kotlin.collections.ArrayList


class SearchActivity : AppCompatActivity() {
    private lateinit var _binding: ActivitySearchBinding
    private val binding: ActivitySearchBinding
        get() = _binding
    private val sharedViewModel by lazy {
        MainActivity.mainActivity?.let { ViewModelProvider(it)[SharedViewModel::class.java] }!!
    }

    private lateinit var searchFragment: SearchFragment<Any>

    val data: MutableList<CustomLink<Any>> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initData()
        initObserver()
    }

    private fun initObserver() {


    }

    private fun initData() {

    }

    private fun initBinding() {
        _binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}