package com.jhp.cloudmusic.ui.home.attention

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AttentionViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "关注Fragment"
    }
    val text: LiveData<String> = _text
}