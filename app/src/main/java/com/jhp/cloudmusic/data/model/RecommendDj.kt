package com.jhp.cloudmusic.data.model

data class RecommendDj(
    val code: Int,
    val djRadios: List<DjRadio>,
    val name: String){
    data class DjRadio(
        val buyed: Boolean,
        val category: String,
        val categoryId: Int,
        val copywriter: String,
        val createTime: Long,
        val dj: Dj,
        val feeScope: Int,
        val id: String,
        val name: String,
        val picUrl: String,
        val playCount: Int,
        val programCount: Int,
        val radioFeeType: Int,
        val rcmdtext: Any,
        val subCount: Int,
        val subed: Boolean
    )

    data class Dj(
        val accountStatus: Int,
        val anchor: Boolean,
        val authStatus: Int,
        val authenticationTypes: Int,
        val authority: Int,
        val avatarDetail: Any,
        val avatarImgId: Long,
        val avatarImgIdStr: String,
        val avatarImgId_str: String,
        val avatarUrl: String,
        val backgroundImgId: Long,
        val backgroundImgIdStr: String,
        val backgroundUrl: String,
        val birthday: Long,
        val city: Int,
        val defaultAvatar: Boolean,
        val description: String,
        val detailDescription: String,
        val djStatus: Int,
        val expertTags: Any,
        val experts: Any,
        val followed: Boolean,
        val gender: Int,
        val mutual: Boolean,
        val nickname: String,
        val province: Int,
        val remarkName: Any,
        val signature: String,
        val userId: Long,
        val userType: Int,
        val vipType: Int
    )

}

