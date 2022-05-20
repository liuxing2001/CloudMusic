package com.jhp.cloudmusic.ui.home.discovery.tab_layout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.jhp.cloudmusic.data.model.HotPlayList
import com.jhp.cloudmusic.data.model.NewMusic
import com.jhp.cloudmusic.data.repository.Repository

class DiscoveryFragmentThreeViewModel:ViewModel() {
    private val type = MutableLiveData<String>()
    private val hotPlayListData = ArrayList<HotPlayList.PlayList>()
    private val newMusicData = ArrayList<NewMusic.Data>()

    val loading = MutableLiveData<Boolean>()

    val hotPlayListLiveData : LiveData<Result<HotPlayList>> = Transformations.switchMap(type) {
        loading.value = false
        Repository.getHotPlayList()
    }
    val newMusicLiveData : LiveData<Result<NewMusic>> = Transformations.switchMap(type) {

        Repository.getNewMusic()
    }

    val hotPlayList: ArrayList<HotPlayList.PlayList>
        get() = hotPlayListData

    val newMusic: ArrayList<NewMusic.Data>
        get() = newMusicData

    fun getHotList() {
        loading.value = true
        type.value = "1"
    }


    fun setHotPlayList(data: List<HotPlayList.PlayList>) {
        hotPlayList.clear()
        val list = data.filter {
            it.id.isNotEmpty()
        }
        hotPlayListData.addAll(list)
    }
    fun setNewMusic(data: List<NewMusic.Data>) {
        newMusic.clear()
        val list = data.filter {
            it.id.isNotEmpty()
        }
        newMusicData.addAll(list)
    }
}