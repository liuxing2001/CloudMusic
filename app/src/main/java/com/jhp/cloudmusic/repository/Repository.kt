package com.jhp.cloudmusic.repository

import androidx.lifecycle.liveData
import com.jhp.cloudmusic.api.ApplicationNetWork
import com.jhp.cloudmusic.dao.UserInfoDao
import com.jhp.cloudmusic.model.LoginUser
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
        if (res.code == 200) {
            // 异步线程
            Thread {
                UserInfoDao.saveUserInfo(data,res)
            }.start()
            Result.success(res.account)
        } else Result.failure(RuntimeException("response status is ${res.code}"))
    }
}