package com.jhp.cloudmusic.api

import DjProgram
import com.jhp.cloudmusic.data.model.*
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
    //搜索建议
    @GET("/search/suggest")
    fun getSearchSuggest(@Query("keywords") keywords: String):Call<SearchSuggest>
    // 搜索接口
    @GET("/cloudsearch")
    fun search(@Query("keywords") keywords: String): Call<SearchSongs>

    // 默认搜索关键词

    @GET("/search/default")
    fun getSearchDefault(): Call<SearchDefault>

    // 获取用户歌单
    @GET("/user/playlist")
    fun getPlayList(@Query("uid") uid: String): Call<UserPlayList>

    //获取歌单所有歌曲
    @GET("/playlist/track/all")
    fun getPlayListTrack(@Query("id") id: String): Call<SongList>

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

    //获取推荐MV
    @GET("/mv/all")
    fun getMvList(): Call<MVList>

    //获取MV url
    @GET("/mv/url")
    fun getMvUrl(@Query("id") id: String): Call<MVUrl>

    //获取MV点赞转发评论数
    @GET("/mv/detail/info")
    fun getMvCountDetail(@Query("mvid") id: String): Call<MVCountDetail>

    //获取MV评论
    @GET("/comment/mv")
    fun getMvComment(@Query("id") id: String): Call<MusicComment>

    //获取歌单 (网友精选碟)
    @GET("/top/playlist")
    fun getHotPlayList(@Query("limit") count:Int ): Call<HotPlayList>

    //获取新歌速递
    @GET("/top/song")
    fun getNewMusic(): Call<NewMusic>

    //获取推荐电台
    @GET("/dj/recommend")
    fun getRecommendDj(): Call<RecommendDj>

    //获取电台banner
    @GET("/dj/banner")
    fun getDjBanner(): Call<DjBanner>

    //获取电台所有节目
    @GET("/dj/program")
    fun getDjProgramList(@Query("rid") id: String, @Query("limit") limit: Int): Call<DjProgram>
}