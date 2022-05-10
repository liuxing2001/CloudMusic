package com.jhp.cloudmusic.ui.home.discovery.tab_layout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.jhp.cloudmusic.model.HotTopList
import com.jhp.cloudmusic.repository.Repository

/**
 * @author GuoGuo
 * @date 2022/1/26
 */
class DiscoveryFragmentTwoViewModel : ViewModel() {
    private val type = MutableLiveData<String>()
    private val hotTopData = ArrayList<HotTopList.HopTopDetail>()
    val loading = MutableLiveData<Boolean>()

    val hotTopListLiveData : LiveData<Result<HotTopList>> = Transformations.switchMap(type) {
        loading.value = false
        Repository.getTopList()
    }
    val hotTopList: ArrayList<HotTopList.HopTopDetail>
        get() = hotTopData

    fun getTopList() {
        loading.value = true
        type.value = "1"
    }

    fun setHotTopList(data: List<HotTopList.HopTopDetail>) {
        hotTopList.clear()
        val list = data.filter {
            it.tracks.isNotEmpty()
        }
        hotTopData.addAll(data)
    }
}