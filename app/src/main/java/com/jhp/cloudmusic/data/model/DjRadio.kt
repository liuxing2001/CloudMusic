package com.jhp.cloudmusic.data.model

data class DjRadio(
    val buyed: Boolean,
    val category: String,
    val categoryId: Int,
    val copywriter: String,
    val createTime: Long,
    val dj: Dj,
    val feeScope: Int,
    val id: Int,
    val name: String,
    val picUrl: String,
    val playCount: Int,
    val programCount: Int,
    val radioFeeType: Int,
    val rcmdtext: Any,
    val subCount: Int,
    val subed: Boolean
)