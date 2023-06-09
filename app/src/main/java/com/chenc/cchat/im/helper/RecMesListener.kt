package com.chenc.cchat.im.helper

interface RecMesListener {

    fun <T> onReceiveMes(data: BaseMessage<T>)
}