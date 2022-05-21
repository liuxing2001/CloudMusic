package com.jhp.cloudmusic.data.model


data class SearchDefault(
    val code: Int,
    val `data`: Data,
    val message: Any
)
{
    data class Data(
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

    data class StyleKeyword(
        val descWord: String,
        val keyWord: Any
    )
}
