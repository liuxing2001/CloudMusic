package com.jhp.cloudmusic.ui.home.podcast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.jhp.cloudmusic.data.model.DjBanner

import com.jhp.cloudmusic.data.model.HotTopList
import com.jhp.cloudmusic.data.model.RecommendDj
import com.jhp.cloudmusic.data.repository.Repository

class PodcastViewModel : ViewModel() {

    private val type = MutableLiveData<String>()
    private val recommendDjData = ArrayList<RecommendDj.DjRadio>()
    private val djBannerData = ArrayList<DjBanner.Data>()

    val loading = MutableLiveData<Boolean>()

    val djBannerLiveData : LiveData<Result<DjBanner>> = Transformations.switchMap(type) {

        Repository.getDjBanner()
    }
    val recommendDjLiveData : LiveData<Result<RecommendDj>> = Transformations.switchMap(type) {
        loading.value = false
        Repository.getRecommendDj()
    }

    val recommendDjList: ArrayList<RecommendDj.DjRadio>
        get() = recommendDjData
    val djBannerList: ArrayList<DjBanner.Data>
        get() = djBannerData


    fun getRecommendDj() {
        loading.value = true
        type.value = "1"
    }

    fun setRecommendDj(data: List<RecommendDj.DjRadio>) {
        recommendDjList.clear()
        val list = data.filter {
            it.id.isNotEmpty()
        }
        recommendDjData.addAll(list)
    }
    fun setDjBanner(data: List<DjBanner.Data>) {
        djBannerList.clear()
        val list = data.filter {
            it.pic.isNotEmpty()
        }
        djBannerData.addAll(list)
    }
}