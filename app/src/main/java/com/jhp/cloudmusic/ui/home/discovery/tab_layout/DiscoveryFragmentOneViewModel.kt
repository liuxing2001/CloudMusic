package com.jhp.cloudmusic.ui.home.discovery.tab_layout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.jhp.cloudmusic.model.RecommendSongs
import com.jhp.cloudmusic.repository.Repository

/**
 * @author GuoGuo
 * @date 2021/12/8 11:49 下午
 */
class DiscoveryFragmentOneViewModel: ViewModel() {
    private val songLiveData = MutableLiveData<String>()
    val recommendSongsList = ArrayList<RecommendSongs.Data.DailySong>()
    val recommendListLiveData = Transformations.switchMap(songLiveData){
        Repository.getRecommendSongs()
    }
    fun getRecommendSongs(){
        recommendSongsList.clear()
        songLiveData.value = "1"
    }
}