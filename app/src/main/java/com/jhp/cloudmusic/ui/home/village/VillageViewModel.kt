package com.jhp.cloudmusic.ui.home.village

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VillageViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "云村Fragment"
    }
    val text: LiveData<String> = _text
}