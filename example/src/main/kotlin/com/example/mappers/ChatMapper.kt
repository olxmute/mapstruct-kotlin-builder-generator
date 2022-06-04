package com.example.mappers

import com.example.dto.ChatMessageDto
import com.example.dto.ChatMessageListProjection
import org.mapstruct.Mapper

@Mapper
interface ChatMapper {

    fun toChatMessageDto(chatMessage: ChatMessageListProjection): ChatMessageDto

}