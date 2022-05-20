package com.jhp.cloudmusic.data.model

data class MVUrl(
    val code: Int,
    val data: Data
) {
    data class Data(
        val id: Int,
        val url: String,
        val r: Int,
        val size: Any,
        val md5: String,
        val code: Int,
        val expi: Int,
        val fee: Any,
        val mvFee: Any,
        val st: Any,
        val promotionVo: Any,
        val msg: String
    )
}
