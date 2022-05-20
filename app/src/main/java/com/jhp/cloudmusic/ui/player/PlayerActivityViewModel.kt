package com.jhp.cloudmusic.ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.jhp.cloudmusic.data.model.LyricModel
import com.jhp.cloudmusic.data.repository.Repository

class PlayerActivityViewModel :ViewModel() {
    private val songsId = MutableLiveData<Int>()
    private var objectData: MutableLiveData<LyricModel> = MutableLiveData()
    val lyric: LiveData<LyricModel>
        get() = objectData

    //拿到从仓库层返的liveData
    val lyricLiveData: LiveData<Result<LyricModel>> = Transformations.switchMap(songsId) {
        Repository.getLyric(it)
    }

    //获取歌词
    fun getLyric(id: Int) {
        songsId.value = id
    }

    //设置歌词
    fun setLyric(data: LyricModel) {
        objectData.value = data
    }
}