package com.ryms.kotlinchat.models

class ChatModel(
    val id: String,
    val text: String,
    val fromId: String,
    val toId: String,
    val timeStamp: Long
) {
    constructor() : this("", "", "", "", -1)
}
