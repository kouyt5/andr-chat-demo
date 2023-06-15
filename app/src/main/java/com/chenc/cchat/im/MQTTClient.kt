package com.chenc.cchat.im

import android.util.Log
import com.chenc.cchat.im.helper.ConnectStatusListener
import com.chenc.cchat.im.helper.IMStatusCode
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import com.hivemq.client.mqtt.mqtt5.Mqtt5RxClient
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish
import com.hivemq.client.mqtt.mqtt5.message.subscribe.suback.Mqtt5SubAck
import com.hivemq.client.rx.FlowableWithSingle
import io.reactivex.Single
import kotlinx.coroutines.Job
import java.util.UUID
import java.util.concurrent.TimeUnit


class MQTTClient {
    private val TAG: String = "MQTTClient"
    private var mMqttClient: Mqtt5RxClient? = null
    fun connect(listener: ConnectStatusListener) {
        mMqttClient = Mqtt5Client.builder()
            .identifier(UUID.randomUUID().toString())
            .serverHost(IMConstant.HOST)
            .serverPort(IMConstant.PORT)
            .buildRx()
        mMqttClient!!.connect()
            .doOnSuccess { connAck: Mqtt5ConnAck ->
                run {
                    Log.i(TAG, "Connected, " + connAck.reasonCode)
                    listener.onSuccess()
                    subscribe()
                }
            }
            .doOnError { throwable: Throwable ->
                run {
                    Log.i(TAG,
                        "Connection failed, " + throwable.message
                    )
                    listener.onError(IMStatusCode.UN_KNOWN)
                }
            }
            .timeout(IMConstant.TIMEOUT, TimeUnit.SECONDS)
            .ignoreElement()
            .blockingAwait()
    }

    fun subscribe() {
        val subAckAndMatchingPublishes: FlowableWithSingle<Mqtt5Publish, Mqtt5SubAck> =
            mMqttClient!!.subscribePublishesWith()
                .topicFilter("mes/c2c/+/a").qos(MqttQos.AT_LEAST_ONCE)
                .addSubscription().topicFilter("a/b/c/d").qos(MqttQos.EXACTLY_ONCE)
                .applySubscription()
                .applySubscribe()
    }

    fun send() {

    }
}