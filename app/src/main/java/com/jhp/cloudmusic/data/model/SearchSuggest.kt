package com.jhp.cloudmusic.data.model

data class SearchSuggest(val code: Int,
                         val result: Result){
    data class Result(val allMatchs: List<AllMatch>){
       data class AllMatch(
           val keyword: String,
           val type: Int,
           val alg: String,
           val lastKeyword: String,
           val feature: String)
    }
}
