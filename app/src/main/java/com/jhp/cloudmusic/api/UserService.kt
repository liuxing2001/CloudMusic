package com.jhp.cloudmusic.api

import com.jhp.cloudmusic.model.UserInfo
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

}