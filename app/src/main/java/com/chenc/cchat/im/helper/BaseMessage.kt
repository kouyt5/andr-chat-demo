package com.chenc.cchat.im.helper

/**
 * MQTT传递消息
 */
data class BaseMessage<T>(
    val id: String,
    val content: String,          // 内容
    val date: String,             // 信息发送时间
    val sourceID: String,         // 信息源用户id
    val destID: String,           // 发送目标用户id
    val ContentType: MessageType, // 消息类型(string, img, etc)
    val type: TargetType,         // 消息目标类型(好友，群)

    val data: T,           // 消息其他信息
)
