package com.chenc.cchat.im.helper

interface ConnectStatusListener {
    fun onSuccess()
    fun  onError(code: IMStatusCode)
}