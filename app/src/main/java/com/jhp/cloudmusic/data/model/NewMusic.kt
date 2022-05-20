package com.jhp.cloudmusic.data.model
data class NewMusic(
    val code: Int,
    val data: List<Data>
){
    data class Data(
        val album: Album,
        val alias: List<String>,
        val artists: List<ArtistXX>,
        val audition: Any,
        val bMusic: BMusic,
        val commentThreadId: String,
        val copyFrom: String,
        val copyrightId: Int,
        val crbt: Any,
        val dayPlays: Int,
        val disc: String,
        val duration: Int,
        val exclusive: Boolean,
        val fee: Int,
        val ftype: Int,
        val hMusic: HMusic,
        val hearTime: Int,
        val id: String,
        val lMusic: LMusic,
        val mMusic: MMusic,
        val mp3Url: String,
        val mvid: Int,
        val name: String,
        val no: Int,
        val playedNum: Int,
        val popularity: Int,
        val position: Int,
        val privilege: Privilege,
        val ringtone: String,
        val rtUrl: Any,
        val rtUrls: Any,
        val rtype: Int,
        val rurl: Any,
        val score: Int,
        val starred: Boolean,
        val starredNum: Int,
        val status: Int,
        val transNames: List<String>
    )

    data class Album(
        val alias: List<String>,
        val artist: Artist,
        val artists: List<ArtistX>,
        val blurPicUrl: String,
        val briefDesc: String,
        val commentThreadId: String,
        val company: String,
        val companyId: Int,
        val copyrightId: Int,
        val description: String,
        val id: Int,
        val name: String,
        val onSale: Boolean,
        val paid: Boolean,
        val pic: Long,
        val picId: Long,
        val picId_str: String,
        val picUrl: String,
        val publishTime: Long,
        val size: Int,
        val songs: List<Any>,
        val status: Int,
        val subType: String,
        val tags: String,
        val transNames: List<String>,
        val type: String
    )

    data class ArtistXX(
        val albumSize: Int,
        val alias: List<Any>,
        val briefDesc: String,
        val followed: Boolean,
        val id: Int,
        val img1v1Id: Long,
        val img1v1Id_str: String,
        val img1v1Url: String,
        val musicSize: Int,
        val name: String,
        val picId: Int,
        val picUrl: String,
        val topicPerson: Int,
        val trans: String
    )

    data class BMusic(
        val bitrate: Int,
        val dfsId: Int,
        val extension: String,
        val id: Long,
        val name: Any,
        val playTime: Int,
        val size: Int,
        val sr: Int,
        val volumeDelta: Int
    )

    data class HMusic(
        val bitrate: Int,
        val dfsId: Int,
        val extension: String,
        val id: Long,
        val name: Any,
        val playTime: Int,
        val size: Int,
        val sr: Int,
        val volumeDelta: Int
    )

    data class LMusic(
        val bitrate: Int,
        val dfsId: Int,
        val extension: String,
        val id: Long,
        val name: Any,
        val playTime: Int,
        val size: Int,
        val sr: Int,
        val volumeDelta: Int
    )

    data class MMusic(
        val bitrate: Int,
        val dfsId: Int,
        val extension: String,
        val id: Long,
        val name: Any,
        val playTime: Int,
        val size: Int,
        val sr: Int,
        val volumeDelta: Int
    )

    data class Privilege(
        val cp: Int,
        val cs: Boolean,
        val dl: Int,
        val fee: Int,
        val fl: Int,
        val flag: Int,
        val id: Int,
        val maxbr: Int,
        val payed: Int,
        val pl: Int,
        val preSell: Boolean,
        val sp: Int,
        val st: Int,
        val subp: Int,
        val toast: Boolean
    )

    data class Artist(
        val albumSize: Int,
        val alias: List<Any>,
        val briefDesc: String,
        val followed: Boolean,
        val id: Int,
        val img1v1Id: Long,
        val img1v1Id_str: String,
        val img1v1Url: String,
        val musicSize: Int,
        val name: String,
        val picId: Int,
        val picUrl: String,
        val topicPerson: Int,
        val trans: String
    )

    data class ArtistX(
        val albumSize: Int,
        val alias: List<Any>,
        val briefDesc: String,
        val followed: Boolean,
        val id: Int,
        val img1v1Id: Long,
        val img1v1Id_str: String,
        val img1v1Url: String,
        val musicSize: Int,
        val name: String,
        val picId: Int,
        val picUrl: String,
        val topicPerson: Int,
        val trans: String
    )

}

