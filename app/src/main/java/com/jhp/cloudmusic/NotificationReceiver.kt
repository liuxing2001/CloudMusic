package com.jhp.cloudmusic

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/*
* 通知栏--点击事件之调用广播实现相对应点击事件的逻辑处理
* */
class NotificationReceiver : BroadcastReceiver() {
    private var context: Context? = null
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        this.context = context

        val type = intent.getIntExtra(TYPE, -1)
        if (type != -1) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(type)
        }
        if (action == "stop") {
           print("dadada")
        }
        if (action == "next") {
            print("dadada")
        }
        if (action == "last") {
            print("dadada")
        }
    }

    companion object {
        const val TYPE = "type" //这个type是为了Notification更新信息的
    }
}