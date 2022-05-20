package com.jhp.cloudmusic.data.model
data class DjBanner(
    val code: Int,
    val `data`: List<Data>
)
{
    data class Data(
        val exclusive: Boolean,
        val pic: String,
        val targetId: Long,
        val targetType: Int,
        val typeTitle: String,
        val url: String
    )

}
