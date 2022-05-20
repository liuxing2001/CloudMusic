package com.jhp.cloudmusic.ui.mvplayer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.jhp.cloudmusic.data.model.MVUrl
import com.jhp.cloudmusic.data.model.MusicComment
import com.jhp.cloudmusic.data.repository.Repository

class MVPlayerViewModel:ViewModel() {
    //获取MV url
    private val mvId = MutableLiveData<String>()
    val mvUrl: MutableLiveData<MVUrl.Data> = MutableLiveData()
    var commentList = ArrayList<MusicComment.Comment>()

    val mvCountDetailLiveData = Transformations.switchMap(mvId){
        Repository.getMvCountDetail(it)
    }

    val mvCommentLiveData = Transformations.switchMap(mvId){
        Repository.getMvComment(it)
    }

    val mvUrlLiveData = Transformations.switchMap(mvId) {
        Repository.getMvUrl(it)
    }

    fun setMvId(data: String) {
        mvId.value = data
    }

}