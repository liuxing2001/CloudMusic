package com.jhp.cloudmusic.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.jhp.cloudmusic.data.model.MusicUrl
import com.jhp.cloudmusic.data.model.NowPlayInfo
import com.jhp.cloudmusic.data.repository.Repository
import com.lzx.starrysky.SongInfo

/**
 *
 * @author : jhp
 * @date : 2022-04-01 20:04
 */
class SharedViewModel : ViewModel() {

    //播放器播放队列歌单(SongInfo)
    var mediaPlayerList: MutableList<SongInfo> = mutableListOf()

    //播放器播放队列歌单(NowPlayInfo)
    var nowPlayerList: MutableList<NowPlayInfo> = mutableListOf()

    //获取当前播放音乐的url
    private val _playerSongId = MutableLiveData<String>()
    val playerSongUrl: MutableLiveData<MusicUrl> = MutableLiveData()

    val playerSongUrlLiveData = Transformations.switchMap(_playerSongId) {
        Repository.getMusicUrl(it)
    }

    fun setPlayerSongId(data: String) {
        _playerSongId.value = data
    }

    //当前播放音乐信息
    private val _nowPlayerSong = MutableLiveData<NowPlayInfo>()
    val getNowPlayInfo: NowPlayInfo?
        get() = _nowPlayerSong.value
    val nowPlayerSongLiveData = Transformations.map(_nowPlayerSong) {
        _nowPlayerSong
    }

    fun setNowPlayerSong(data: NowPlayInfo) {
        _nowPlayerSong.value = data
    }

    //定义当前音乐播放状态，10代表上一首,11代表没有播放或暂停，12代表正在播放，13代表下一首，15表示暂停
    private val playStatus = MutableLiveData<Int>(11)
    val getPlayStatus: Int
        get() = playStatus.value!!
    val playStatusLiveData = Transformations.map(playStatus) {
        it
    }

    fun changeStatus(data: Int) {
        playStatus.value = data
    }
    //搜索功能
    private val keyword = MutableLiveData<String>()
    val keyWordLiveData = Transformations.switchMap(keyword) {
        Repository.search(it)
    }
    fun  searchKeyword(data: String){
        keyword.value = data
    }
    val getSearchKey: String?
        get() = keyword.value
}