package com.chenc.cchat.im

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.chenc.cchat.im.helper.BaseMessage
import com.chenc.cchat.im.helper.ConnectStatusListener
import com.chenc.cchat.im.helper.IMStatusCode
import com.chenc.cchat.im.helper.RecMesListener
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 消息接收服务
 * @author 陈聪
 * @since v0.1.0
 */
class RecMesService : Service() {
    private val TAG: String = "RecMesService"
    private val mRecCallbacks = ArrayList<RecMesListener>()
    private val mBinder = RecMesBinder(this)
    private var mConnectListener: ConnectStatusListener? = null
    private val mClient: MQTTClient = MQTTClient()

    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        ioScope.launch(CoroutineExceptionHandler { _, throwable ->
            Log.w(TAG, throwable.message ?: "error unknown")
        }) {
            mClient.connect(object : ConnectStatusListener {
                override fun onSuccess() {
                    mConnectListener?.onSuccess()
                }

                override fun onError(code: IMStatusCode) {
                    mConnectListener?.onError(code)
                }
            })
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    class RecMesBinder(private val service: RecMesService) : Binder() {

        /**
         * 注册消息接收回调函数
         */
        fun registerRecCallback(callback: RecMesListener) {
            service.mRecCallbacks.add(callback)
        }

        /**
         * 客户端连接状态监听
         */
        fun setOnConnectListener(listener: ConnectStatusListener) {
            service.mConnectListener = listener
        }

        fun <T> sendMessage(mes: BaseMessage<T>) {

        }
    }
}