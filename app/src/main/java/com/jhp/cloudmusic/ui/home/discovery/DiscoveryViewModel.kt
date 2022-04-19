package com.jhp.cloudmusic.ui.home.discovery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DiscoveryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "发现Fragment"
    }
    val text: LiveData<String> = _text
}