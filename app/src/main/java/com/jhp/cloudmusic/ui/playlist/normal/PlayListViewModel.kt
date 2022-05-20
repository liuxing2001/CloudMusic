package com.jhp.cloudmusic.ui.playlist.normal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.jhp.cloudmusic.data.model.SongList
import com.jhp.cloudmusic.data.repository.Repository

/**
 *
 * @author : jhp
 * @date : 2022-04-19 15:55
 */
class PlayListViewModel : ViewModel() {
    private val songsId = MutableLiveData<String>()
    val playSongList = ArrayList<SongList.Song>()

    //拿到从仓库层返的liveData
    val playSongListLiveData = Transformations.switchMap(songsId) {
        Repository.getPlayListDetail(it)
    }

    fun getPlayListTrack(data: String) {
        songsId.value = data
    }
}