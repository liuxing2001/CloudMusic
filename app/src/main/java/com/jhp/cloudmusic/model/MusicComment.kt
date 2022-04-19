package com.jhp.cloudmusic.model

data class MusicComment(
    val code: Int,
    val commentBanner: Any,
    val comments: List<Comment>,
    val hotComments: List<HotComment>,
    val isMusician: Boolean,
    val more: Boolean,
    val moreHot: Boolean,
    val topComments: List<Any>,
    val total: Int,
    val userId: Long
) {
    data class Comment(
        val beReplied: List<BeReplied>,
        val commentId: Long,
        val commentLocationType: Int,
        val content: String,
        val contentResource: Any,
        val decoration: Decoration,
        val expressionUrl: Any,
        val liked: Boolean,
        val likedCount: Int,
        val needDisplayTime: Boolean,
        val parentCommentId: Long,
        val pendantData: Any,
        val repliedMark: Any,
        val showFloorComment: Any,
        val status: Int,
        val time: Long,
        val timeStr: String,
        val user: User
    ) {
        data class BeReplied(
            val beRepliedCommentId: Long,
            val content: String,
            val expressionUrl: Any,
            val status: Int,
            val user: User
        ) {
            data class User(
                val anonym: Int,
                val authStatus: Int,
                val avatarDetail: Any,
                val avatarUrl: String,
                val commonIdentity: Any,
                val expertTags: Any,
                val experts: Any,
                val followed: Boolean,
                val liveInfo: Any,
                val locationInfo: Any,
                val mutual: Boolean,
                val nickname: String,
                val remarkName: Any,
                val userId: Long,
                val userType: Int,
                val vipRights: Any,
                val vipType: Int
            )
        }

        class Decoration

        data class User(
            val anonym: Int,
            val authStatus: Int,
            val avatarDetail: Any,
            val avatarUrl: String,
            val commonIdentity: Any,
            val expertTags: Any,
            val experts: Any,
            val followed: Boolean,
            val liveInfo: Any,
            val locationInfo: Any,
            val mutual: Boolean,
            val nickname: String,
            val remarkName: Any,
            val userId: Long,
            val userType: Int,
            val vipRights: VipRights,
            val vipType: Int
        ) {
            data class VipRights(
                val associator: Associator,
                val musicPackage: Any,
                val redVipAnnualCount: Int,
                val redVipLevel: Int
            ) {
                data class Associator(
                    val rights: Boolean,
                    val vipCode: Int
                )
            }
        }
    }

    data class HotComment(
        val beReplied: List<BeReplied>,
        val commentId: Long,
        val commentLocationType: Int,
        val content: String,
        val contentResource: Any,
        val decoration: Any,
        val expressionUrl: Any,
        val liked: Boolean,
        val likedCount: Int,
        val needDisplayTime: Boolean,
        val parentCommentId: Long,
        val pendantData: Any,
        val repliedMark: Any,
        val showFloorComment: Any,
        val status: Int,
        val time: Long,
        val timeStr: String,
        val user: User
    ) {
        data class BeReplied(
            val beRepliedCommentId: Long,
            val content: String,
            val expressionUrl: Any,
            val status: Int,
            val user: User
        ) {
            data class User(
                val anonym: Int,
                val authStatus: Int,
                val avatarDetail: AvatarDetail,
                val avatarUrl: String,
                val commonIdentity: Any,
                val expertTags: Any,
                val experts: Any,
                val followed: Boolean,
                val liveInfo: Any,
                val locationInfo: Any,
                val mutual: Boolean,
                val nickname: String,
                val remarkName: Any,
                val userId: Long,
                val userType: Int,
                val vipRights: Any,
                val vipType: Int
            ) {
                data class AvatarDetail(
                    val identityIconUrl: String,
                    val identityLevel: Int,
                    val userType: Int
                )
            }
        }

        data class User(
            val anonym: Int,
            val authStatus: Int,
            val avatarDetail: Any,
            val avatarUrl: String,
            val commonIdentity: Any,
            val expertTags: Any,
            val experts: Any,
            val followed: Boolean,
            val liveInfo: Any,
            val locationInfo: Any,
            val mutual: Boolean,
            val nickname: String,
            val remarkName: Any,
            val userId: Long,
            val userType: Int,
            val vipRights: VipRights,
            val vipType: Int
        ) {
            data class VipRights(
                val associator: Associator,
                val musicPackage: Any,
                val redVipAnnualCount: Int,
                val redVipLevel: Int
            ) {
                data class Associator(
                    val rights: Boolean,
                    val vipCode: Int
                )
            }
        }
    }
}