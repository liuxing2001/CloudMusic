package com.jhp.cloudmusic.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jhp.cloudmusic.databinding.ActivityPhoneLoginBinding
import com.jhp.cloudmusic.extension.md5Encode
import com.jhp.cloudmusic.model.LoginUser
import com.jhp.cloudmusic.ui.MainActivity
import com.jhp.cloudmusic.utils.XToastUtils
import com.xuexiang.xui.utils.WidgetUtils
import com.xuexiang.xui.widget.dialog.LoadingDialog

class PhoneLoginActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java]
    }
    lateinit var binding: ActivityPhoneLoginBinding
    private lateinit var mLoadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initListener()
        initObserver()
    }

    private fun initBinding() {
        mLoadingDialog = WidgetUtils.getLoadingDialog(this)
            .setIconScale(0.4f)
            .setLoadingSpeed(8)
        binding = ActivityPhoneLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initListener() {
        val username = binding.etLoginPhone as TextView
        val pwd = binding.etLoginPassword as TextView
        binding.btnLoginNext.setOnClickListener {
            when {
                username.text.isEmpty() -> {
                    return@setOnClickListener XToastUtils.error("请输入手机号")
                }
                username.text.length != 11 -> {
                    return@setOnClickListener XToastUtils.error("请输入11位数字的手机号")
                }
                else -> {
                    binding.rvLoginPhone.visibility = View.GONE
                    binding.rvLoginPassword.visibility = View.VISIBLE
                }
            }
        }
        binding.btnLoginLogin.setOnClickListener {
            when {
                pwd.text.isEmpty() -> {
                    return@setOnClickListener XToastUtils.error("请输入密码")
                }

                else -> {
                    mLoadingDialog.show()
                    val data = LoginUser(
                        userName = username.text.toString(),
                        md5_password = pwd.text.toString().md5Encode()
                    )
                    viewModel.login(data)
                }
            }
        }
    }

    private fun initObserver() {

        viewModel.userInfoLiveData.observe(this) {
            val account = it.getOrNull()
            mLoadingDialog.dismiss()


            if (account != null) {

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            }
        }
    }
}