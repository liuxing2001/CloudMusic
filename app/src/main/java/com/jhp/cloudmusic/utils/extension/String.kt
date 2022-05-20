package com.jhp.cloudmusic.utils.extension

import java.security.MessageDigest

/**
 *
 * @author : jhp
 * @date : 2022-04-01 20:52
 */

//对密码进行md5加密
fun String.md5Encode(): String {
    val hash = MessageDigest.getInstance("MD5").digest(this.toByteArray())
    val hex = StringBuilder(hash.size * 2)
    for (b in hash) {
        var str = Integer.toHexString(b.toInt())
        if (b < 0x10) {
            str = "0$str"
        }
        hex.append(str.substring(str.length - 2))
    }
    return hex.toString()
}