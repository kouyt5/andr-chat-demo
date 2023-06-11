package com.chenc.cchat.im

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.chenc.cchat.im.helper.BaseMessage
import com.chenc.cchat.im.helper.ConnectStatusListener
import com.chenc.cchat.im.helper.RecMesListener
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import com.hivemq.client.mqtt.mqtt5.Mqtt5RxClient
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish
import com.hivemq.client.mqtt.mqtt5.message.subscribe.suback.Mqtt5SubAck
import com.hivemq.client.rx.FlowableWithSingle
import io.reactivex.Single
import java.util.UUID


/**
 * 消息接收服务
 * @author 陈聪
 * @since v0.1.0
 */
class RecMesService : Service() {

    private val mRecCallbacks = ArrayList<RecMesListener>()
    private val mBinder = RecMesBinder(this)
    private var mConnectListener: ConnectStatusListener? = null
    private var mMqttClient: Mqtt5RxClient? = null

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
        mMqttClient = Mqtt5Client.builder()
            .identifier(UUID.randomUUID().toString())
            .serverHost(IMConstant.HOST)
            .serverPort(IMConstant.PORT)
            .buildRx()
        val connAckSingle: Single<Mqtt5ConnAck> = mMqttClient!!.connect()
        val subAckAndMatchingPublishes: FlowableWithSingle<Mqtt5Publish, Mqtt5SubAck> =
            mMqttClient!!.subscribePublishesWith()
                .topicFilter("a/b/c").qos(MqttQos.AT_LEAST_ONCE)
                .addSubscription().topicFilter("a/b/c/d").qos(MqttQos.EXACTLY_ONCE)
                .applySubscription()
                .applySubscribe()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
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