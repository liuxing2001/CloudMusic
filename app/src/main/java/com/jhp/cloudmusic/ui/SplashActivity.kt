package com.jhp.cloudmusic.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jhp.cloudmusic.dao.UserInfoDao

import com.jhp.cloudmusic.databinding.ActivitySplashBinding
import com.jhp.cloudmusic.ui.login.LoginActivity
import java.util.*
import kotlin.concurrent.schedule

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()




        //  判断是否登陆过
        val isLogin: Boolean = UserInfoDao.isLogin()
        Timer().schedule(2000) {
            val intent = Intent(
                this@SplashActivity,
                if (isLogin) MainActivity::class.java
                else LoginActivity::class.java
            )
            startActivity(intent)
            finish()
        }

    }


    private fun initBinding() {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}