package com.jhp.cloudmusic.api

import com.jhp.cloudmusic.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 用户服务api
 * @author : jhp
 * @date : 2022-04-01 14:55
 */
interface UserService {
    //登录服务
    @GET("/login/cellphone")
    fun login(
        @Query("phone") phone: String,
        @Query("md5_password") md5_password: String
    ): Call<UserInfo>
    // 搜索接口
    @GET("/search")
    fun search(@Query("keywords") keywords: String): Call<SearchSongs>

    // 获取用户歌单
    @GET("/user/playlist")
    fun getPlayList(@Query("uid") uid: String): Call<UserPlayList>

    //获取歌单所有歌曲
    @GET("/playlist/track/all")
    fun getPlayListTrack(@Query("id") id: String, @Query("limit") limit: Int): Call<SongList>

    //获取推荐歌曲
    @GET("/recommend/songs")
    fun getRecommendSongs(): Call<RecommendSongs>

    //获取歌词
    @GET("/lyric")
    fun getLyric(@Query("id") id: Int): Call<LyricModel>

    //获取音乐播放路径
    @GET("/song/url")
    fun getMusicUrl(@Query("id") id: String): Call<MusicUrl>

    //获取音乐评论
    @GET("/comment/music")
    fun getComment(@Query("id") id: String): Call<MusicComment>

    //榜单内容摘要
    @GET("/toplist/detail")
    fun getTopList(): Call<HotTopList>
}