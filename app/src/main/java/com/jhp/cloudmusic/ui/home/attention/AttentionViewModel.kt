package com.jhp.cloudmusic.ui.home.attention

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.jhp.cloudmusic.data.model.MVList
import com.jhp.cloudmusic.data.repository.Repository

class AttentionViewModel : ViewModel() {
    private val type = MutableLiveData<String>()
    private val mvData = ArrayList<MVList.MVDetail>()
    val loading = MutableLiveData<Boolean>()

    val mvListLiveData : LiveData<Result<MVList>> = Transformations.switchMap(type) {
        loading.value = false
        Repository.getMvList()
    }
    val mvList: ArrayList<MVList.MVDetail>
        get() = mvData

    fun getMVList() {
        loading.value = true
        type.value = "1"
    }
    fun setMvList(data: List<MVList.MVDetail>) {
        mvList.clear()
        data.filter {
            it.artists.isNotEmpty()
        }
        mvData.addAll(data)
    }
}