package com.jhp.cloudmusic.api

import android.util.Log
import com.jhp.cloudmusic.model.LoginUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Singleton网络服务对象,封装所有网络请求API
 * @author : jhp
 * @date : 2022-04-01 15:22
 */
object ApplicationNetWork {
    //创建UserService接口动态代理对象
    private val UserService = ServiceCreator.create(UserService::class.java)
    suspend fun login(data: LoginUser) = UserService.login(data.userName, data.md5_password).await()



    //这里采用协程异步调用的方式,统一了网络服务的接口,因此所有API都定义为挂起函数
    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine {
            //这里对Call<T>对象进行异步调用,enqueue方法不用考虑线程问题,
            // 它会自动创建工作线程然后在UI线程执行onResponse方法
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    Log.d("response", body.toString())
                    if (body !== null) it.resume(body)
                    else it.resumeWithException(RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    it.resumeWithException(t)
                }
            })
        }
    }
}