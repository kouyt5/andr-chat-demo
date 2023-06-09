package com.chenc.cchat.im

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * 多端消息同步服务
 */
class AsyncService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}