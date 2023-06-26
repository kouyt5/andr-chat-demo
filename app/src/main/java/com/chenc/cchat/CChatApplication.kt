package com.chenc.cchat

import android.app.Application
import android.util.Log
import com.tencent.mmkv.MMKV

class CChatApplication : Application() {
    private val TAG: String = "CChatApplication"

    override fun onCreate() {
        super.onCreate()
        val mmkvDir = MMKV.initialize(this)
        Log.i(TAG, "mmkv dir = $mmkvDir")
    }
}