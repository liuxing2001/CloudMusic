package com.jhp.cloudmusic.data.model

data class Data(
    val exclusive: Boolean,
    val pic: String,
    val targetId: Long,
    val targetType: Int,
    val typeTitle: String,
    val url: String
)