package com.jhp.cloudmusic

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 *  相当于Singleton对象在APP启动时创建,生命周期伴随随整个APP
 *  因此不用担心内存泄漏
 * @author : jhp
 * @date : 2022-04-01 17:31
 */
class MyApplication :Application(){
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}