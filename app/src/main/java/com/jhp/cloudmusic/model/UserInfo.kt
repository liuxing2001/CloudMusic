package com.jhp.cloudmusic.model
data class UserInfo(
    val account: Account,
    val bindings: List<Binding>,
    val code: Int,
    val cookie: String,
    val loginType: Int,
    val profile: Profile,
    val token: String,
    val message: String
) {
    data class Account(
        val anonimousUser: Boolean,
        val ban: Int,
        val baoyueVersion: Int,
        val createTime: Long,
        val donateVersion: Int,
        val id: Int,
        val salt: String,
        val status: Int,
        val tokenVersion: Int,
        val type: Int,
        val userName: String,
        val vipType: Int,

        val viptypeVersion: Long,
        val whitelistAuthority: Int
    )

    data class Binding(
        val bindingTime: Long,
        val expired: Boolean,
        val expiresIn: Int,
        val id: Long,
        val refreshTime: Int,
        val tokenJsonStr: String,
        val type: Int,
        val url: String,
        val userId: Int
    )

    data class Profile(
        val accountStatus: Int,
        val authStatus: Int,
        val authority: Int,
        val avatarDetail: Any,
        val avatarImgId: Long,
        val avatarImgIdStr: String,
        val avatarImgId_str: String,
        val avatarUrl: String,
        val backgroundImgId: Long,
        val backgroundImgIdStr: String,
        val backgroundUrl: String,
        val city: Int,
        val defaultAvatar: Boolean,
        val description: String,
        val detailDescription: String,
        val djStatus: Int,
        val eventCount: Int,
        val expertTags: Any,
        val experts: Experts,
        val followed: Boolean,
        val followeds: Int,
        val follows: Int,
        val gender: Int,
        val mutual: Boolean,
        val nickname: String,
        val playlistBeSubscribedCount: Int,
        val playlistCount: Int,
        val province: Int,
        val remarkName: Any,
        val signature: String,
        val userId: Int ? = 121,
        val userType: Int,
        val vipType: Int
    ) {
        class Experts
    }
}