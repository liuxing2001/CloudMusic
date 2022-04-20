package com.jhp.cloudmusic.model
data class UserPlayList(
    val code: Int,
    val more: Boolean,
    val playlist: List<Playlist>,
    val version: String
) {
    data class Playlist(
        val adType: Int,
        val anonimous: Boolean,
        val artists: Any,
        val backgroundCoverId: Any,
        val backgroundCoverUrl: Any,
        val cloudTrackCount: Int,
        val commentThreadId: String,
        val coverImgId: Long,
        val coverImgId_str: String,
        val coverImgUrl: String,
        val createTime: Long,
        val creator: Creator,
        val description: String,
        val englishTitle: Any,
        val highQuality: Boolean,
        val id: Long,
        val name: String,
        val newImported: Boolean,
        val opRecommend: Boolean,
        val ordered: Boolean,
        val playCount: String,
        val privacy: Int,
        val recommendInfo: Any,
        val shareStatus: Any,
        val sharedUsers: Any,
        val specialType: Int,
        val status: Int,
        val subscribed: Boolean,
        val subscribedCount: Int,
        val subscribers: List<Any>,
        val tags: List<String>,
        val titleImage: Any,
        val titleImageUrl: Any,
        val totalDuration: Int,
        val trackCount: Int,
        val trackNumberUpdateTime: Long,
        val trackUpdateTime: Long,
        val tracks: Any,
        val updateFrequency: Any,
        val updateTime: Long,
    ) {
        data class Creator(
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
            val birthday: Int,
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
//            val userId: Int,
            val userType: Int,
            val vipType: Int
        )
    }
}