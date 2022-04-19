package com.jhp.cloudmusic.ui.home.mine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MineViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "我的Fragment"
    }
    val text: LiveData<String> = _text
}