package com.jhp.cloudmusic.ui.playlist.podcast

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.jhp.cloudmusic.data.model.SongList
import com.jhp.cloudmusic.data.repository.Repository


class PodCastListViewModel : ViewModel() {
    private val listId = MutableLiveData<String>()
    val djProgramList = ArrayList<DjProgram.Program>()

    //拿到从仓库层返的liveData
    val djProgramListLiveData = Transformations.switchMap(listId) {
        Repository.getDjProgramList(it)
    }

    fun getPlayListTrack(data: String) {
        listId.value = data
    }
}