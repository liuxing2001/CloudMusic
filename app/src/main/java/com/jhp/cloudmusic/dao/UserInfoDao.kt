package com.jhp.cloudmusic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.jhp.cloudmusic.MyApplication
import com.jhp.cloudmusic.model.LoginUser
import com.jhp.cloudmusic.model.UserInfo

/**
 * dao层,本地保存用户登录信息
 * @author : jhp
 * @date : 2022-04-01 17:27
 */
object UserInfoDao {
    /**
     * 由于这里只需存储用户信息的JSON,没必要使用调用ROOM存储到SQLITE中
     * 选择了更加方便的sharePreferences(),将用户登录信息存储到本地xml文件中
     */

    private fun sharePreferences() =
        MyApplication.context.getSharedPreferences("app_userInfo", Context.MODE_PRIVATE)

    // 判断用户是否登录

    fun isLogin() = sharePreferences().contains("userName")

    //存储用户信息

    fun saveUserInfo(loginUser: LoginUser, userInfo: UserInfo) {
        sharePreferences().edit {
            putString("userName", loginUser.userName)
            putString("md5_password", loginUser.md5_password)
            putString("userInfo", Gson().toJson(userInfo))
        }
    }

    //获取用户信息
    fun getUserInfo(): UserInfo {
        val userJson = sharePreferences().getString("userInfo", "")
        return Gson().fromJson(userJson, UserInfo::class.java)
    }
    //获取账号密码
    fun getLoginUser(): LoginUser {
        val userName: String = sharePreferences().getString("userName", "") as String
        val password: String = sharePreferences().getString("md5_password", "") as String
        return LoginUser(userName, password)
    }


    //退出登录
    fun logout() {
        sharePreferences().edit {
            clear()
        }
    }

}