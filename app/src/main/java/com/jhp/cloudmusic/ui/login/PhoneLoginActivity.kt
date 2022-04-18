package com.jhp.cloudmusic.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jhp.cloudmusic.databinding.ActivityPhoneLoginBinding

class PhoneLoginActivity : AppCompatActivity() {
    lateinit var binding:ActivityPhoneLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()

    }

    private fun initBinding() {
        binding = ActivityPhoneLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}