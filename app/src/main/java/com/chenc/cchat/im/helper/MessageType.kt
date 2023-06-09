package com.chenc.cchat.im.helper

/**
 * 消息类型
 *
 * @author cc
 */
enum class MessageType {
    STRING,  // 纯文字
    AUDIO,   // 语音
    IMAGE,   // 图片
    VIDEO,   // 视频
    FILE,    // 文件

    OTHER,   // 其他未知类型
}