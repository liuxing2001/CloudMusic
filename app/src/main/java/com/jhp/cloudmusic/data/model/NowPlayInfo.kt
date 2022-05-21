package com.jhp.cloudmusic.data.model

/**
 * 当前播放歌曲信息
 * @author : jhp
 * @date : 2022-04-19 09:22
 */
data class NowPlayInfo(var name: String, var id: String, var ar: List<Ar>, var al: Al) {
    data class Al(
        var id: String,
        var name: String,
        val pic: Long,
        var picUrl: String,
        val pic_str: String,
        val tns: List<Any>
    )

    data class Ar(
        var id: Int,
        var name: String
    )
}
