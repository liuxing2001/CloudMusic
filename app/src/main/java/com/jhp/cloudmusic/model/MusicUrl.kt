package com.jhp.cloudmusic.model

/**
 *
 * @author : jhp
 * @date : 2022-04-19 10:17
 */
data class MusicUrl(
    val code: Int,
    val `data`: List<Data>
) {
    data class Data(
        val br: Int,
        val canExtend: Boolean,
        val code: Int,
        val encodeType: String,
        val expi: Int,
        val fee: Int,
        val flag: Int,
        val freeTimeTrialPrivilege: FreeTimeTrialPrivilege,
        val freeTrialInfo: Any,
        val freeTrialPrivilege: FreeTrialPrivilege,
        val gain: Int,
        val id: Int,
        val level: String,
        val md5: String,
        val payed: Int,
        val size: Int,
        val type: String,
        val uf: Any,
        val url: String ?,
        val urlSource: Int
    ) {
        data class FreeTimeTrialPrivilege(
            val remainTime: Int,
            val resConsumable: Boolean,
            val type: Int,
            val userConsumable: Boolean
        )

        data class FreeTrialPrivilege(
            val listenType: Any,
            val resConsumable: Boolean,
            val userConsumable: Boolean
        )
    }
}
