package com.example.dto

import java.time.LocalDateTime

enum class MessageStatus {
    DELIVERED,
    VIEWED,
}

data class ChatMessageDto(
    val id: String,
    val text: String,
    val userFromId: String,
    val messageStatus: MessageStatus,
    val createdDate: LocalDateTime,
)

interface ChatMessageListProjection {
    val id: String
    val text: String
    val messageStatus: MessageStatus
    val createdDate: LocalDateTime
    val userFromId: String
}