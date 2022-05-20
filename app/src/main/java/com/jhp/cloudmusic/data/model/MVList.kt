package com.jhp.cloudmusic.data.model

data class MVList(
    val count:Int,
    val code: Int,
    val hasMore:Boolean,
    val data: List<MVDetail>,
) {
    data class MVDetail(
        val id: Int,
        val cover: String,
        val name: String,
        val playCount: Long,
        val briefDesc: Any,
        val desc: Any,
        val artistName: String,
        val artistId: String,
        val duration: Long,
        val mark: Int,
        val subed: Boolean,
        val artists: List<Artist>,

        ){
        data class Artist(val id: Int,
                          val name: String,
                          val alias: Any,
                          val transNames: Any)
    }


}