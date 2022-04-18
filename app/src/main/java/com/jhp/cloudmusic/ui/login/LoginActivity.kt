package com.jhp.cloudmusic.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jhp.cloudmusic.databinding.ActivityLoginBinding
import com.jhp.cloudmusic.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()



    }



    private fun initBinding(){
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}