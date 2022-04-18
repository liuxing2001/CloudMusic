package com.jhp.cloudmusic.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jhp.cloudmusic.databinding.ActivityLoginBinding
import com.jhp.cloudmusic.utils.XToastUtils

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()

        initListener()

    }


    private fun initBinding(){
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initListener() {
        val protocol = binding.cbProtocol
        binding.loginPhone.setOnClickListener {
            if (!protocol.isChecked) {
                return@setOnClickListener XToastUtils.warning("请同意用户协议与隐私政策")
            }else{
                val intent = Intent(this,PhoneLoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}