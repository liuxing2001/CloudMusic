package com.jhp.cloudmusic.ui.home.discovery.tab_layout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.jhp.cloudmusic.model.HotPlayList
import com.jhp.cloudmusic.repository.Repository

class DiscoveryFragmentThreeViewModel:ViewModel() {
    private val type = MutableLiveData<String>()
    private val hotPlayListData = ArrayList<HotPlayList.PlayList>()
    val loading = MutableLiveData<Boolean>()

    val hotPlayListLiveData : LiveData<Result<HotPlayList>> = Transformations.switchMap(type) {
        loading.value = false
        Repository.getHotPlayList()
    }
    val hotPlayList: ArrayList<HotPlayList.PlayList>
        get() = hotPlayListData

    fun getHotPlayList() {
        loading.value = true
        type.value = "1"
    }

    fun setHotPlayList(data: List<HotPlayList.PlayList>) {
        hotPlayList.clear()
        val list = data.filter {
            it.id.isNotEmpty()
        }
        hotPlayListData.addAll(data)
    }
}