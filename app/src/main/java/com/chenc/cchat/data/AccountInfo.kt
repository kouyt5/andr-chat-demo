package com.chenc.cchat.data

import com.tencent.mmkv.MMKV


/**
 * 用户信息
 */
class AccountInfo {
    companion object {
        var userName: String? = null
        var password: String? = null

        fun init() {
            val kv = MMKV.defaultMMKV()
            userName = kv.decodeString("username")
            password = kv.decodeString("password")
        }

    }
}