package com.jhp.cloudmusic.data.model

data class DataX(
    val action: Int,
    val alg: String,
    val bizQueryInfo: String,
    val gap: Int,
    val logInfo: Any,
    val realkeyword: String,
    val searchType: Int,
    val showKeyword: String,
    val source: Any,
    val styleKeyword: StyleKeyword
)