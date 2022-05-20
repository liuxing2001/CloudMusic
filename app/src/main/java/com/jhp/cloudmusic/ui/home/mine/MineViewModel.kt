package com.jhp.cloudmusic.ui.home.mine

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.jhp.cloudmusic.data.model.UserPlayList
import com.jhp.cloudmusic.data.repository.Repository

class MineViewModel : ViewModel() {

    //获取播放歌单
    private val userUID = MutableLiveData<String>()
    val playList = ArrayList<UserPlayList.Playlist>()
    val playListLiveData = Transformations.switchMap(userUID) {
        Repository.getPlayList(it)
    }

    fun getPlayList(data: String) {
        playList.clear()
        userUID.value = data
    }
}