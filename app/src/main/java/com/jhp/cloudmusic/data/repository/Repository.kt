package com.jhp.cloudmusic.data.repository

import androidx.lifecycle.liveData
import com.jhp.cloudmusic.api.ApplicationNetWork
import com.jhp.cloudmusic.dao.UserInfoDao
import com.jhp.cloudmusic.data.model.LoginUser
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * 仓库层
 * @author : jhp
 * @date : 2022-04-01 15:40
 */
object Repository {
    /** 由于仓库层每个网络请求都需要try,catch因此抽象出一个fire函数统一进行管理,
     *  由于返回结果集是LiveData采用liveData{}代码块,利用它提供的参数转换结果集为LiveData<>
     *  这里lambda表达式传入的是挂起函数,但是回调的时候没有了context,因此前面必须加上suspend关键字
     */
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure(e)
            }
            emit(result)
        }
    //登录
    fun login(data: LoginUser) = fire(Dispatchers.IO) {
        val res = ApplicationNetWork.login(data)
        println("~~~$res")
        if (res.code == 200) {
            // 异步线程
            Thread {
                UserInfoDao.saveUserInfo(data,res)
            }.start()
            Result.success(res.account)
        } else Result.failure(RuntimeException("response status is ${res.code}"))
    }
    //搜索
    fun search(data: String) = fire(Dispatchers.IO) {
        val res = ApplicationNetWork.search(data)
        if (res.code == 200) Result.success(res.result.songs)
        else Result.failure(RuntimeException("response status is ${res.code}"))
    }
    //搜索建议
//    fun getSearchSuggest(data: String) = fire(Dispatchers.IO){
//        val res = ApplicationNetWork.getSearchSuggest(data)
//        if (res.code == 200) Result.success(res.result.allMatches)
//        else Result.failure(RuntimeException("response status is ${res.code}"))
//    }
    fun getSearchDefault() = fire(Dispatchers.IO) {
        val res = ApplicationNetWork.getSearchDefault()
        if (res.code == 200) Result.success(res)
        else Result.failure(RuntimeException("response status is ${res.code}"))
    }
    //获取用户歌单
    fun getPlayList(data: String) = fire(Dispatchers.IO) {
        val res = ApplicationNetWork.getPlayList(data)
        if (res.code == 200) Result.success(res.playlist)
        else Result.failure(RuntimeException("response status is ${res.code}"))
    }

    //获取歌单所有歌曲
    fun getPlayListDetail(data: String) = fire(Dispatchers.IO) {
        val res = ApplicationNetWork.getPlayListTrack(data)
        if (res.code == 200) Result.success(res.songs)
        else Result.failure(RuntimeException("response status is ${res.code}"))
    }

    //获取每日推荐歌曲
    fun getRecommendSongs() = fire(Dispatchers.IO) {
        val res = ApplicationNetWork.getRecommendSongs()
        if (res.code == 200) Result.success(res.data.dailySongs)
        else Result.failure(RuntimeException("response status is ${res.code}"))
    }

    //获取歌词
    fun getLyric(id: Int) = fire(Dispatchers.IO) {
        val res = ApplicationNetWork.getLyric(id)
        if (res.code == 200) Result.success(res)
        else Result.failure(RuntimeException("response status is ${res.code}"))
    }

    //获取音乐播放路径
    fun getMusicUrl(id:String) = fire(Dispatchers.IO){
        val res = ApplicationNetWork.getMusicUrl(id)
        if (res.code == 200) Result.success(res)
        else Result.failure(RuntimeException("response status is ${res.code}"))
    }

    //获取评论
    fun getMusicComment(id:String) = fire(Dispatchers.IO){
        val res = ApplicationNetWork.getMusicComment(id)
        if (res.code == 200) Result.success(res)
        else Result.failure(RuntimeException("response status is ${res.code}"))
    }

    //获取排行榜
    fun getTopList() = fire(Dispatchers.IO){
        val res = ApplicationNetWork.getTopList()
        if (res.code == 200) Result.success(res)
        else Result.failure(RuntimeException("response status is ${res.code}"))
    }
    //获取MV列表
    fun getMvList() = fire(Dispatchers.IO){
        val res = ApplicationNetWork.getMvList()
        if (res.code == 200) Result.success(res)
        else Result.failure(RuntimeException("response status is ${res.code}"))
    }

    //获取MV url
    fun getMvUrl(id:String) = fire(Dispatchers.IO){
        val res = ApplicationNetWork.getMvUrl(id)
        if (res.code == 200) Result.success(res)
        else Result.failure(RuntimeException("response status is ${res.code}"))
    }

    //获取MV 点赞评论数
    fun getMvCountDetail(id:String) = fire(Dispatchers.IO){
        val res = ApplicationNetWork.getMvCountDetail(id)
        if (res.code == 200) Result.success(res)
        else Result.failure(RuntimeException("response status is ${res.code}"))
    }

    //获取MV评论
    fun getMvComment(id:String) = fire(Dispatchers.IO){
        val res = ApplicationNetWork.getMvComment(id)
        if (res.code == 200) Result.success(res)
        else Result.failure(RuntimeException("response status is ${res.code}"))
    }

    //获取歌单 (网友精选碟)
    fun getHotPlayList()= fire(Dispatchers.IO){
        val res = ApplicationNetWork.getHotPlayList()
        if (res.code == 200) Result.success(res)
        else Result.failure(RuntimeException("response status is ${res.code}"))
    }
    //获取新歌速递
    fun getNewMusic()= fire(Dispatchers.IO){
        val res = ApplicationNetWork.getNewMusic()
        if (res.code == 200) Result.success(res)
        else Result.failure(RuntimeException("response status is ${res.code}"))
    }
    //获取推荐电台
    fun getRecommendDj()= fire(Dispatchers.IO){
        val res = ApplicationNetWork.getRecommendDj()
        if (res.code == 200) Result.success(res)
        else Result.failure(RuntimeException("response status is ${res.code}"))
    }
    //获取电台banner
    fun getDjBanner()= fire(Dispatchers.IO){
        val res = ApplicationNetWork.getDjBanner()
        if (res.code == 200) Result.success(res)
        else Result.failure(RuntimeException("response status is ${res.code}"))
    }
    //获取电台所有节目
    fun getDjProgramList(data: String)= fire(Dispatchers.IO){
        val res = ApplicationNetWork.getDjProgramList(data)
        if (res.code == 200) Result.success(res)
        else Result.failure(RuntimeException("response status is ${res.code}"))
    }
}