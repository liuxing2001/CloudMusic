package com.jhp.cloudmusic.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.jhp.cloudmusic.model.LoginUser
import com.jhp.cloudmusic.repository.Repository

/**
 *
 * @author : jhp
 * @date : 2022-04-17 21:56
 */
class LoginViewModel:ViewModel() {
    private val loginData = MutableLiveData<LoginUser>()
    val userInfoLiveData = Transformations.switchMap(loginData) {
        Repository.login(it)
    }
    fun login(data: LoginUser) {
        loginData.value = data
    }
}