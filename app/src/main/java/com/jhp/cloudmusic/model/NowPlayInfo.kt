package com.jhp.cloudmusic.model

/**
 * 当前播放歌曲信息
 * @author : jhp
 * @date : 2022-04-19 09:22
 */
data class NowPlayInfo(val name: String, val id: Int, val ar: List<Ar>, val al: Al) {
    data class Al(
        val id: Int,
        val name: String,
        val pic: Long,
        val picUrl: String,
        val pic_str: String,
        val tns: List<Any>
    )

    data class Ar(
        val id: Int,
        val name: String
    )
}
